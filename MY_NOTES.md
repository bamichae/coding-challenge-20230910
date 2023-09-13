- I thought about making a separate reporting controller, but there weren't enough requirements to make a whole service. If there were more reports, I would separate the employee creation from reporting.


- Wanted to name employee in ReportingStructure to employeeId, but instructions did not say to do this.


- There are inconsistent field names requested ie. employee, employeeId, id. I would just settle on one.


- Was going to use Date as effectivedate, but I would only add that if we had a front end that had a click drop-down or select, as the user likely isn't going to type in a valid Date type, so I just made it a String. Ideally it would not be a String so you can perform standard operations on it.


- Ideally I would have check to see if fields are null or blank if that's not allowed ie. not allowing a user to enter no name for an Employee.


- Ideally I would add better error handling in CompensationController to return a more detailed error to the user.