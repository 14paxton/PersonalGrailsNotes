## Altering fetch
  // Using a dynamic finder
  Author.findAllByNameLike("John%", [ sort: 'name', order: 'asc', fetch: [location: 'join'] ])
  
  
    import org.hibernate.FetchMode as FM

    def results = Airport.withCriteria {
    eq "region", "EMEA"
    fetchMode "flights", FM.SELECT
}

## More Advanced Subqueries in GORM
   The support for subqueries has been extended. You can now use in with nested subqueries

  def results = Person.where {
    firstName in where { age < 18 }.firstName. 
   }.list()
   Criteria and where queries can be seamlessly mixed:

    def results = Person.withCriteria {
    notIn "firstName", Person.where { age < 18 }.firstName
     }
### Subqueries can be used with projections:

   def results = Person.where {
    age > where { age > 18 }.avg('age')
   }
### Correlated queries that span two domain classes can be used:

   def employees = Employee.where {
    region.continent in ['APAC', "EMEA"]
    }.id()
    def results = Sale.where {
    employee in employees && total > 100000
    }.employee.list()

### And support for aliases (cross query references) using simple variable declarations has been added to where queries:

   def query = Employee.where {
    def em1 = Employee
    exists Sale.where {
        def s1 = Sale
        def em2 = employee
        return em2.id == em1.id
    }.id()
   }
    def results = query.list()
