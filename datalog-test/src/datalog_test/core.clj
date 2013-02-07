(ns datalog-test.core
  (:use [fogus.datalog.bacwn]
        [fogus.datalog.bacwn.impl.rules]
        [fogus.datalog.bacwn.impl.database]
        [fogus.datalog.bacwn.impl.util]
        [fogus.datalog.bacwn.macros]
        [clojure.pprint :only [pprint]]))

(def db-base
  (make-database
   (relation :employee [:name :position])
   (index :employee :name)

   (relation :boss [:employee-name :boss-name])
   (index :boss :employee-name)

   (relation :can-do-job [:position :job])
   (index :can-do-job :position)

   (relation :job-replacement [:job :can-be-done-by])
   ;;(index :job-replacement :can-be-done-by)

   (relation :job-exceptions [:name :job])))

(def db
  (add-tuples db-base
              [:employee :name "Bob"    :position :boss]
              [:employee :name "Mary"   :position :chief-accountant]
              [:employee :name "John"   :position :accountant]
              [:employee :name "Sameer" :position :chief-programmer]
              [:employee :name "Lilian" :position :programmer]
              [:employee :name "Li"     :position :technician]
              [:employee :name "Fred"   :position :sales]
              [:employee :name "Brenda" :position :sales]
              [:employee :name "Miki"   :position :project-management]
              [:employee :name "Albert" :position :technician]

              [:boss :employee-name "Mary"  :boss-name "Bob"]
              [:boss :employee-name "John"  :boss-name "Mary"]
              [:boss :employee-name "Sameer" :boss-name "Bob"]
              [:boss :employee-name "Lilian"  :boss-name "Sameer"]
              [:boss :employee-name "Li"  :boss-name "Sameer"]
              [:boss :employee-name "Fred"  :boss-name "Bob"]
              [:boss :employee-name "Brenda"  :boss-name "Fred"]
              [:boss :employee-name "Miki"  :boss-name "Bob"]
              [:boss :employee-name "Albert" :boss-name "Li"]

              [:can-do-job :position :boss               :job :management]
              [:can-do-job :position :accountant         :job :accounting]
              [:can-do-job :position :chief-accountant   :job :accounting]
              [:can-do-job :position :programmer         :job :programming]
              [:can-do-job :position :chief-programmer   :job :programming]
              [:can-do-job :position :technician         :job :server-support]
              [:can-do-job :position :sales              :job :sales]
              [:can-do-job :position :project-management :job :project-management]

              [:job-replacement :job :pc-support :can-be-done-by :server-support]
              [:job-replacement :job :pc-support :can-be-done-by :programming]
              [:job-replacement :job :payroll    :can-be-done-by :accounting]

              [:job-exceptions :name "Sameer" :job :pc-support]))

(def rules
  (rules-set
   (<- (:works-for :employee ?x :boss ?y)
       (:boss :employee-name ?x :boss-name ?y))
   (<- (:works-for :employee ?x :boss ?y)
       (:works-for :employee ?x :boss ?z)
       (:works-for :employee ?z :boss ?y))
   (<- (:employee-job* :employee ?x :job ?y)
       (:employee :name ?x :position ?pos)
       (:can-do-job :position ?pos :job ?y))
   (<- (:employee-job* :employee ?x :job ?y)
       (:job-replacement :job ?y :can-be-done-by ?z)
       (:employee-job* :employee ?x  :job ?z))
   (<- (:employee-job* :employee ?x :job ?y)
       (:can-do-job :job ?y)
       (:employee :name ?x :position ?z)
       (if = ?z :boss))
   (<- (:employee-job :employee ?x :job ?y)
       (:employee-job* :employee ?x :job ?y)
       (not! :job-exceptions :name ?x :job ?y))
   (<- (:bj :name ?x :boss ?y)
       (:works-for :employee ?x :boss ?y)
       (not! :employee-job :employee ?y :job :pc-support))))

(def wp-1 (build-work-plan rules (?- :works-for :employee '?name :boss ?x)))
(pprint (run-work-plan wp-1 db {}))
;;({:boss "Li", :employee "Albert"} {:boss "Sameer", :employee
;;"Albert"} {:boss "Bob", :employee "Albert"})

(def wp-foo (build-work-plan rules (?- :employee-job* :employee '??name
                                       :job '?job)))
(run-work-plan wp-foo db {'??name "Li"})

(def wp-2 (build-work-plan rules (?- :employee-job :employee '??name :job ?x)))
(run-work-plan wp-2 db {'??name "Li"})
;; ({:job :server-support, :employee "Li"} {:job :pc-support,
;; :employee "Li"})

(def wp-bar (build-work-plan rules (?- :employee-job* :employee '??name
                                       :job '?job)))
(run-work-plan wp-bar db {'??name "Sameer"})


(def wp-3 (build-work-plan rules (?- :bj :name '??name :boss ?x)))
(run-work-plan wp-3 db {'??name "Albert"})
;; ({:boss "Sameer", :name "Albert"})

(def wp-4 (build-work-plan rules (?- :works-for :employee ?x :boss ?y)))
(run-work-plan wp-4 db {})

