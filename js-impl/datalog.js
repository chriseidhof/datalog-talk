/* Facts */

// Bob is alices dad. Carol is bobs mom.

facts = [
  ["person", "bob"],
  ["person", "alice"],
  ["person", "carol"],
  ["parent", "bob", "alice"],
  ["parent", "carol", "bob"],
];

rules = [
  [ ["ancestor", "X","Y"], [ "parent", "X", "Y" ]
  ],
  [ ["ancestor", "X","Y"], [ "ancestor", "X", "Z" ],
                           [ "ancestor", "Z", "Y" ]
  ]
]

function query(arr,result) {
  if(typeof(result)==='undefined') result = [];
  for(var i = 0; i < facts.length; i++) {
    if(unify(facts[i], arr)) {
      result.push(facts[i]);
    }
  }
  for(var i = 0; i < rules.length; i++) {
    var rule = rules[i];
    var ruleName = rule[0][0];
    var isMatchingRule = ruleName == arr[0];
    if(isMatchingRule) {
      var unfilteredResults = evaluateClause(rule,result);
      for(var j = 0; j < unfilteredResults.length; j++) {
        var unfilteredResult = unfilteredResults[j];
        if(unify(arr,unfilteredResult)) {
          result.push(unfilteredResult);
        }
      }
    }
  }
  return result;
}

function evaluateClause(clause,result) {
  var ruleHead = clause[0];
  var variableBindingsPerGoal = [];
  for(var i = 1; i < clause.length; i++) {
    var goal = clause[i];
    variableBindingsPerGoal.push(evaluateGoal(goal,result));
  }
  console.log(variableBindingsPerGoal);

  // TODO Better name
  var finalBindings = match(variableBindingsPerGoal);

  return [];
}

// todo better name
function match(variableBindingsPerGoal) {

}

function evaluateGoal(goal,result) {
  var goalResult = query(goal,result);
  var results = [];
  for(var i = 0; i < goalResult.length; i++) {
    var bindings = {};
    for(var j = 1; j < goal.length; j++) {
      variableName = goal[j];
      if(isVariable(variableName)) {
        bindings[variableName] = goalResult[i][j];
      }
    }
    results.push(bindings);
  }
  return results;
}

function unifyHashMaps(a,b) {
  for(var key in a) {
    if(b[key] == undefined) continue;
    if(!unifyValue(a[key],b[key])) return false;
  }
  return true;
}

function unify(a,b) {
  for(var i = 0; i < a.length; i++) {
    if(!unifyValue(a[i],b[i])) return false;
  }
  return true;
}

function unifyValue(x,y) {
  return x == y || isVariable(x) || isVariable(y);
}

function isVariable(name) {
  var character = name[0];
  return character == character.toUpperCase();
}

console.log(query(["parent","X","Y"]));
