employee(1  , bob    ,boss).
employee(2  , mary   ,chief_accountant).
employee(3  , john   ,accountant).
employee(4  , sameer ,chief_programmer).
employee(5  , lilian ,programmer).
employee(6  , li     ,technician).
employee(7  , fred   ,sales).
employee(8  , brenda ,sales).
employee(9  , miki   ,project_management).
employee(10 , albert ,technician).

boss(2  , 1).
boss(3  , 2).
boss(4  , 1).
boss(5  , 4).
boss(6  , 4).
boss(7  , 1).
boss(8  , 7).
boss(9  , 1).
boss(10 , 6).

can_do_job(boss               , management).
can_do_job(accountant         , accounting).
can_do_job(chief_accountant   , accounting).
can_do_job(programmer         , programming).
can_do_job(chief_programmer   , programming).
can_do_job(technician         , server_support).
can_do_job(sales              , sales).
can_do_job(project_management , project_management).

job_replacement(pc_support , server_support).
job_replacement(pc_support , programming).
job_replacement(payroll    , accounting).

job_exceptions(4, pc_support).

% Rules

directly_works_for(X,Y) :- 
   boss(EID,BID),
   employee(EID,X,_),
   employee(BID,Y,_).

works_for(X,Y) :- directly_works_for(X,Y).
works_for(X,Y) :- directly_works_for(X,Z),
                  works_for(Z,Y).

test(X) :- not(test(X)).
