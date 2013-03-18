OMeta
=

  * Frederic Kettelhoit / Chris Eidhof
  * Berlin Compiler Meet
  * March 18, 2013

!

Datalog
=

* Declarative
* Deductive databases
* Subset of Prolog

!

Facts
=

    parent(bill,mary).
    parent(mary,john).
    male(bill).
    curling-team(bill,mary,john,tom).

!


Rules
=

    ancestor(X,Y) :- parent(X,Y).
    ancestor(X,Y) :- parent(X,Z),ancestor(Z,Y).

* `X` is an ancestor of `Y` __if__ `X` is the parent of `Y`, __or__:
* `X` is an ancestor of `Y` __if__ `X` is the parent of `Z` __and__ `Z` is an
ancestor of `Y`.

!

The first rule of datalog
=

_Every variable in the head of the clause needs to appear at least once
in the body of the clause._

Here's some invalid datalog:

<code class="red">

   foo(X) :- ancestor(A,B).

</code>

Now `foo` can be satisfied for any `X`. But how to find the possible
values?

!

More rules
=
    family(X,Y) :- ancestor(X,Y).
    family(X,Y) :- family(Y,X).

    p(X) :- q(X).
    q(X) :- p(X).

Everything terminates. Always.

!

Queries
=

    ?- family(bill,X).

Returns a list of all the people `bill` is related to.

!


Bottom-up evaluation
=

Step 1

    parent(bill,mary).
    parent(mary,john).
    ancestor(X,Y) :- parent(X,Y).
    ancestor(X,Y) :- parent(X,Z),ancestor(Z,Y).

!

Bottom-up evaluation
=

Step 2

    parent(bill,mary).
    parent(mary,john).
    ancestor(X,Y) :- parent(X,Y).
    ancestor(X,Y) :- parent(X,Z),ancestor(Z,Y).
    ancestor(bill,mary).
    ancestor(mary,john).

!

Bottom-up evaluation
=

Step 3

    parent(bill,mary).
    parent(mary,john).
    ancestor(X,Y) :- parent(X,Y).
    ancestor(X,Y) :- parent(X,Z),ancestor(Z,Y).
    ancestor(bill,mary).
    ancestor(mary,john).
    ancestor(bill,john).
!

Extension: `not`
=

    female(X) :- not(male(X))

But there is a problem. How do we find `X`?

!

Fixing the problem
=

    female(X) :- person(X), not(male(X)).

* Every variable needs to appear non-negated as well.
 
!

More problems with `not`
=

    p(X) :- not(p(X)).

Now we need stratification.

!

Implementations
=

* [Cascalog](https://github.com/nathanmarz/cascalog/wiki)
* [Clojure Datalog](http://code.google.com/p/clojure-contrib/wiki/DatalogOverview)
* ([Datomic](http://docs.datomic.com/tutorial.html))

!

Roll your own datalog
=

* Every presentation needs an implementation

!

More links
= 

* [http://infolab.stanford.edu/~ullman/cs345notes/cs345-1.pdf](http://infolab.stanford.edu/~ullman/cs345notes/cs345-1.pdf)

!
