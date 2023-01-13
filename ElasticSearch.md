# ElasticSearch
## Fuzzy Matching

```java
//if fuzzyList already exists, and the previous values have not changed, destroy list to prevent popup
    Closure fuzzyListNeedsUpdated = { LocalAttributeMap flowScope, Map paramValueMap ->
        def queryFieldsModified = flowScope.contains("fuzzyFieldValues") ? flowScope.get("fuzzyFieldValues") != paramValueMap : true
        if (flowScope.contains("fuzzyDuplicateList")) flowScope.remove("fuzzyDuplicateList")
        return queryFieldsModified
    }

    Closure fuzzyP2PDuplicateCheck = { aor, Long interviewModelId, LocalAttributeMap flowScope ->
        Map searchFieldParamMap = ["firstName": [boost: 1.3f], "lastName": [boost: 2.1f], "externalPersonId": [boost: 3.1f], "email": [boost: 3.5f]]
        def queryTheseFormFields = searchFieldParamMap.keySet()
        Map paramValueMap = queryTheseFormFields.findResults { fieldName -> if (aor.properties.containsKey(fieldName)) [fieldName, aor[fieldName as String]] }.collectEntries()
        def runFuzzyQuery = fuzzyListNeedsUpdated(flowScope, paramValueMap)

        if (runFuzzyQuery) {
            Map fuzzyAssessmentSortFields = ['relevanceScore': 'DESC', 'lastName': 'ASC', 'firstName': 'ASC', 'id': 'ASC']
            List returnFields = [
                    "catalogDetailId",
                    "interviewModelId",
                    "completedDate",
                    "email",
                    "externalPersonId",
                    "firstName",
                    "jobReqCode",
                    "jobTitle",
                    "lastName",
                    "scoringModelId",
                    "scoringModelAlias"]

        def clientSetupId = sessionService.selectedClientSetupId() ?: aor?.user?.clientSetupId
            if (paramValueMap && clientSetupId) {
                Map paramMap = ["assessmentType": AssessmentType.P2P.key, *: paramValueMap]
                paramMap.put("clientSetupId", clientSetupId)
            if (interviewModelId) paramMap.put("interviewModelId", interviewModelId )

                def jsonList = assessmentOrderElasticsearchService.duplicateAssessmentFuzzySearch(paramMap, returnFields, searchFieldParamMap, fuzzyAssessmentSortFields, aor?.user)
                if (jsonList) {
                    flowScope.put("fuzzyDuplicateList", jsonList.toString())
                    flowScope.put("fuzzyFieldValues", paramValueMap)
                    throw new FuzzyDuplicatesFoundException(jsonList as JSON)
                }
            }
        }

    }
```

```java

    /**
     *
     * @param params map of fieldName and value to search [fieldName: value]
     * @param returnFields list of column values to be returned from elastic search
     * @param searchFieldParamMap map dictating fields to search and fuzzy parameters that can be modified, boost and prefix_length are available, ex. [formFieldField : [boost: boostVal, prefix: prefixVal]
     * @param fuzzyAssessmentSortFields fields and direction elastic search should sort, ex. ['relevanceScore': 'DESC', 'lastName': 'ASC', 'firstName': 'ASC', 'id': 'ASC']
     * @param principalUser the current logged in user
     * @return results a JSON object consisting of the requested returnFields param and interviewModelId , benchmark, fuzzyRankScore, orderId
     */
    def duplicateAssessmentFuzzySearch(Map params, List returnFields, searchFieldParamMap, fuzzyAssessmentSortFields, principalUser) {
        //boost adds points to matches, a match on that field will add to the overall score of the record ranking
        //prefix set to 2 to indicate the first two characters must be matched.
        searchFieldParamMap = searchFieldParamMap ?: ["firstName": [boost: 1.3f, prefix: 2], "lastName": [boost: 2.1f, prefix: 2], "externalPersonId": [boost: 3.1f], "email": [boost: 3.5f]]
        principalUser = principalUser ?: userService.getCurrentPrincipalUser()

        Map securityParamMap = [:]
        def startTime = System.currentTimeMillis()
        securityParamMap.put("interviewModelId", params.get('interviewModelId') ? params.get('interviewModelId') as Long : null)
        securityParamMap.put("assessmentType", params.get('assessmentType') ?: null)
        securityParamMap.put("clientSetupId", params.get('clientSetupId') ?: null)
        def sourceFields = returnFields?.findResults { parameterToFieldMap.get(it) ?: null }
        Map<String, Map> searchFields = buildFieldValueMap(params, searchFieldParamMap) as Map<String, Map>

        if (!esConfig || !esIndex || !securityParamMap.get("assessmentType") || !sourceFields) {
            log.error("Incorrect parameters supplied to duplicateAssessmentFuzzySearch method. esIndex: ${esIndex}")
            return 0
        }

        try {
            def searchTime = System.currentTimeMillis()

            SortBuilder[] sort = fuzzyAssessmentSortFields ? buildSort(fuzzyAssessmentSortFields) : [] as SortBuilder[]
            BoolQueryBuilder securityFilterQuery = securityAndAccessFilter(securityParamMap)
            def fuzzyQuery = elasticsearchService.fuzzyQueryWrapperBuilder(searchFields, securityFilterQuery)
            def results = parseDuplicateAssessmentReturnJSON(elasticsearchService.searchByIndex(esConfig, esIndex, fuzzyQuery, sort, 0, 100, sourceFields as String[], 10))

            log.debug "Time taken for searchByIndex ${CoreUtils.endTime(searchTime)}"
            def dtoTime = System.currentTimeMillis()
            log.debug "Time taken for build dtos ${CoreUtils.endTime(dtoTime)}"
            log.debug "Time taken for elasticsearch service ${CoreUtils.endTime(startTime)}"

            return results
        }
        catch (def e) {
            log.error("ERROR: ", e)
        }

    }

    /**
     *
     * @param securityParamMap map passed field options secGroupSecEnabled , dataAccessModel,  assessmentBlocks, interviewModelId, assessmentType, clientSetupId keyed to value to search for
     * @return boolQueryBuilder consisting of a must query for each passed in security param
     */
    BoolQueryBuilder securityAndAccessFilter(Map securityParamMap) {
        if (!securityParamMap) return null
        BoolQueryBuilder qb = QueryBuilders.boolQuery()
        Map securityQueryBuilderMap =
                [secGroupSecEnabled: addSecGroupSecEnabledFilter(securityParamMap, qb),
                        dataAccessModel: addDataAccessModelFilter(securityParamMap, qb),
                        assessmentBlocks: addAssessmentBlocksFilter(securityParamMap, qb),
                        interviewModelId: addInterviewModelIdFilter(securityParamMap, qb),
                        assessmentType: addAssessmentTypeFilter(securityParamMap, qb),
                        clientSetupId: addClientSetupIdFilter(securityParamMap, qb)
                ]

        securityParamMap.entrySet().each { fieldValuePair ->
            if(fieldValuePair.getValue()) securityQueryBuilderMap.get(fieldValuePair.getKey())()
        }

        return qb
    }

    Closure buildFieldValueMap = { Map<String, String> params, Map<String, Map> fieldValueList ->
        fieldValueList.findResults { key, fuzzyParams ->
            def fieldSuffix = fieldSuffixMap.get(key) ?: ''
            (params.containsKey(key) && parameterToFieldMap.containsKey(key)) ? ["${parameterToFieldMap.get(key)}${fieldSuffix}", [searchValue: "${(fieldSuffix == '.keywordlower') ? params.get(key).toLowerCase() : params.get(key)}".toString(), boost: fuzzyParams?.boost, prefix: fuzzyParams?.prefix]] : null
        }.collectEntries()
    }

    Closure parseDuplicateAssessmentReturnJSON = { results ->
        if (!results?.resultList && !results?.recordsTotal) return null
        [fuzzyList: results.resultList.findResults { !userAssessmentOrderAccessService.hasOwner(it?.id) ? buildAssessmentJson(it) : null }, totalHits: results?.recordsTotal] as JSON
    }

    Closure buildAssessmentJson = { order ->
        def benchMark = order["_source"]?.data?.catalogDetail?.id ? CatalogDetail.get(order["_source"]?.data?.catalogDetail?.id)?.scoringModelName : ''
        [orderId: "${order?._id}", fuzzyRankScore: "${order?._score}", benchmark: benchMark, interviewModelId: order["_source"]?.data?.catalogDetail?.interviewModelId, *: order["_source"]?.data?.order]
    }

    Closure addClientSetupIdFilter = { Map securityParamMap, BoolQueryBuilder qb ->
        if (securityParamMap?.clientSetupId ) {
            def fieldSuffix = fieldSuffixMap.get("clientSetupId") ?: ''
            return { -> qb.must(QueryBuilders.termQuery(parameterToFieldMap.get('clientSetupId'), securityParamMap?.clientSetupId))}
        }
        return{->}
    }

    Closure addSecGroupSecEnabledFilter = { Map securityParamMap, BoolQueryBuilder qb ->
        if (securityParamMap?.allowedSecurityGroupIds) {
            return { -> qb.must(QueryBuilders.termsQuery(parameterToFieldMap.get('securityGroupId'), securityParamMap?.allowedSecurityGroupIds)) }
        }
        else {
            return { -> qb.must(QueryBuilders.termQuery(parameterToFieldMap.get('id'), 0)) }
        }
    }

    Closure addDataAccessModelFilter = { Map securityParamMap, BoolQueryBuilder qb ->
        if (securityParamMap?.includeResults && !securityParamMap?.allowedAssessmentCodesByDam?.mgrAllowAllAssessments) {
            def codes = securityParamMap?.allowedAssessmentCodesByDam?.mgrAssessmentCodes ?: "";
            def fieldSuffix = fieldSuffixMap.get("assessmentCode") ?: ''
            return { -> qb.must(QueryBuilders.termsQuery("${parameterToFieldMap.get("assessmentCode")}${fieldSuffix}", codes)) }
        }
        return{->}
    }

    Closure addAssessmentBlocksFilter = { Map securityParamMap, BoolQueryBuilder qb ->
        return { -> qb.mustNot(QueryBuilders.termsQuery(parameterToFieldMap.get('id'), securityParamMap?.assessmentBlocks)) }
    }

    Closure addInterviewModelIdFilter = { Map securityParamMap, BoolQueryBuilder qb ->
        return { -> qb.must(QueryBuilders.termsQuery(parameterToFieldMap.get('interviewModelId'), securityParamMap?.interviewModelId)) }
    }

    Closure addAssessmentTypeFilter = { Map securityParamMap, BoolQueryBuilder qb ->
        def assessmentType = AssessmentType.getEnumConstants().find { it.key.toLowerCase() == securityParamMap.get('assessmentType').toLowerCase() }
        if(assessmentType){
            String assessmentTypeStringVal = assessmentType?.key?.toLowerCase(Locale.ENGLISH)
            def fieldSuffix = fieldSuffixMap.get("assessmentType") ?: ''
            return { -> qb.must(QueryBuilders.termQuery("${parameterToFieldMap.get('assessmentType')}${fieldSuffix}", assessmentTypeStringVal)) }
        }
        return {->}
    }

    Closure buildSort = { Map sortFieldOrderMap ->
        SortBuilder[] sortBuilderArray = []
        if (sortFieldOrderMap) {
            sortBuilderArray = sortFieldOrderMap.collect { kFieldName, vOrder ->
                def keywordSuffix = fieldSuffixMap.get(kFieldName) ?: ''
                def elasticSearchField = parameterToFieldMap.containsKey(kFieldName) ? "${parameterToFieldMap.get(kFieldName)}${keywordSuffix}" : kFieldName
                def sort = SortOrder.fromString(vOrder) ?: SortOrder.ASC
                new FieldSortBuilder(elasticSearchField)
                        .order(sort)
            } as SortBuilder[]
        }

        return sortBuilderArray
    }
```

```java
def searchByIndex(ElasticsearchConfig esConfig, String esIndex, QueryBuilder query, SortBuilder[] sortBy, int from, int size, String[] includedFields = null, def minScore = null ) {
        if (!esConfig || !esIndex || !size || (from + size) > 10000) {
            log.error("Incorrect parameters supplied to searchByIndex method. esIndex: ${esIndex}, from: ${from}, size: ${size}")
            return null
        }

        def docHits = []

        log.debug("Starting search from row ${from} to ${(from + size)}")
        try {
            SearchRequest searchRequest = new SearchRequest()
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()

            if (includedFields) {
                searchSourceBuilder.fetchSource(includedFields, [] as String[])
            }

            searchSourceBuilder.query(query)
                               .from(from)
                               .size(size)
                               .trackScores(false)
                               .trackTotalHits(true)
                               .explain(false)
            if(minScore) searchSourceBuilder.minScore(minScore as float)

            sortBy.each{
                searchSourceBuilder.sort(it)
            }
            searchRequest.indices(esIndex)
            searchRequest.source(searchSourceBuilder)

            log.debug("Using query: " + searchSourceBuilder.toString())

            SearchResponse searchResponse = elasticsearchSearchWrapperService.search(esConfig, searchRequest, RequestOptions.DEFAULT)

            if (searchResponse == null) {
                return [recordsTotal: 0, resultList: []]
            }

            TimeValue took = searchResponse.getTook()
            SearchHits hits = searchResponse.getHits()
            TotalHits totalHits = hits.getTotalHits()
            int numHits = totalHits.value

            log.debug("Finished searchByIndex. Found ${searchResponse?.hits?.hits?.size()} out of ${numHits} hits, in ${took.toString()}")

            searchResponse?.hits?.hits?.each { hit ->
                def hitJSON = new JsonSlurper().parseText(hit.toString())
                docHits << hitJSON
            }

            log.debug("Finished parsing hits.")

            return [recordsTotal: numHits, resultList: docHits]
        } catch (Exception e) {
            log.error("Error searching by index. ", e)
            return [recordsTotal: 0, resultList: []]
        }
    }

    Closure createFuzzyList = { Map<String, Map> fieldValueMap ->
        BoolQueryBuilder shouldMatchAFuzzyQuery = QueryBuilders.boolQuery()
        fieldValueMap.each { String keyFieldName, Map searchAndBoost ->
            def fuzzy = QueryBuilders.fuzzyQuery(keyFieldName, searchAndBoost?.searchValue)
            if (searchAndBoost?.prefix) fuzzy.prefixLength(searchAndBoost?.prefix)
            fuzzy.fuzziness(Fuzziness.AUTO)
                 .transpositions(true)
                 .boost(searchAndBoost?.boost)
            shouldMatchAFuzzyQuery.should(fuzzy)
        }
        if(fieldValueMap.size() > 1) shouldMatchAFuzzyQuery.minimumShouldMatch(2)
        return shouldMatchAFuzzyQuery
    }
```

```java
 def fuzzyQueryWrapperBuilder(Map<String, Map> fieldValueMap, requiredFilters = null) {
        BoolQueryBuilder wrapperQueryBuilder = QueryBuilders.boolQuery()
        BoolQueryBuilder theFuzzyListQuery = createFuzzyList(fieldValueMap)
        wrapperQueryBuilder.must(theFuzzyListQuery)

        if (requiredFilters) {
            BoolQueryBuilder additionalFiltersQuery = QueryBuilders.boolQuery()
            additionalFiltersQuery.filter(requiredFilters)
            wrapperQueryBuilder.must(additionalFiltersQuery)
        }

        return wrapperQueryBuilder
    }
```
