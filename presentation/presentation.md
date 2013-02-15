OMeta
=

  * Chris Eidhof
  * Berlin Compiler Meet
  * December 11th, 2012

!

What is OMeta?
=

* PEG
* OO
* Host language

!

History
=

* Meta-II
* VPRI / Fonc / Steps

!

Example
=

    ometa E {
      num = digit+,
      fac = fac '*' num
          | fac '/' num
          | num,
      exp = exp '+' fac
          | exp '-' fac
          | fac
    } 

!

Example (evaluation)
=

    ometa E {
      num = digit+:xs -> parseInt(xs.join('')),
      fac = fac:x '*' num:y -> (x * y)
          | fac:x '/' num:y -> (x / y)
          | num,
      exp = exp:x '+' fac:y -> (x + y)
          | exp:x '-' fac:y -> (x - y)
          | fac
    } 

!

Example (AST building)
=

    ometa CalcParser {
      number = digit+:xs -> parseInt(xs.join('')),
      exp  = exp:x '+' mul:y  -> ['add', x, y]
           | exp:x '-' mul:y  -> ['sub', x, y]
           | mul,
      mul  = mul:x '*' prim:y -> ['mul', x, y]
           | mul:x '/' prim:y -> ['div', x, y]
           | prim,
      prim = '(' expr:x ')'           -> x
           | number:n                 -> ['num', n]
    }

    x = CalcParser.matchAll("100*3+7","exp")
    x

!

Example (AST Evaluation)
=

    ometa CalcInterpreter {
      i = ['num' anything:x]        -> x
        | ['add' i:x i:y] -> (x + y)
        | ['sub' i:x i:y] -> (x - y)
        | ['mul' i:x i:y] -> (x * y)
        | ['div' i:x i:y] -> (x / y)
    }

    CalcInterpreter.match(x,"i")

!

Grammar
=

  * Sequence
  * Prioritized choice
  * Zero or more repetitions
  * One or more repetitions
  * Negation
  * Application
  * Character literals

!

Rules are methods
=

  (See Web Inspector)

!

Rules can be parameterized
=

    token t = spaces t:x -> t
    for = token "for"

!

Higher-order rules
=
  Rules that take rules
  
    apply("for")
    apply(t)

!

My implementation
=

  * OMeta/ObjC
  * Not self-compiling yet
  * Tricky part: parsing ObjC
 
!

  Awesome for language experiments: just subclass and extend a language

!
Links
=

  * [OMeta Workspace](http://www.tinlizzie.org/ometa-js/#OMeta_Tutorial)
  * [github.com/chriseidhof/ometa-objc](https://github.com/chriseidhof/ometa-objc)
  * chris@eidhof.nl
  
