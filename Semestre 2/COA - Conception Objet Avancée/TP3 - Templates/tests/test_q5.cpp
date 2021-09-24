#include "catch.hpp"
#include "myclass.hpp"

#include <iostream>

#include <vector>
#include <algorithm>
#include "set_functions.hpp"

#include <list>

#include <map>

using namespace std;

TEST_CASE("intersectionMap", "[Q5]"){

    map<int,string> a;
    a[1] = 'q' ; a[2] = 'w' ; a[3] = 'e' ; a[4] = 'r' ; a[5] = 't' ; a[6] = 'y' ;

    map<int,string> b;
    b[1] = 'q' ; b[2] = 'w' ; b[3] = 'e' ; b[4] = 'r' ; b[5] = 't' ; b[6] = 'z' ;

    map<int,string> r;

    map<int,string> oracle;
    oracle[1] = 'q' ; oracle[2] = 'w' ; oracle[3] = 'e' ; 
    oracle[4] = 'r' ; oracle[5] = 't' ;

    set_intersection_t(begin(a), end(a), begin(b), end(b), inserter(r,r.end()));

    REQUIRE(r == oracle);
}

TEST_CASE("unionMap", "[Q5]"){

    map<int,string> a;
    a[1] = 'q' ; a[2] = 'w' ; a[3] = 'e' ; a[4] = 'r' ; a[5] = 't' ; a[6] = 'y' ;

    map<int,string> b;
    b[7] = 'z' ;

    map<int,string> r;

    map<int,string> oracle;
    oracle[1] = 'q' ; oracle[2] = 'w' ; oracle[3] = 'e' ; 
    oracle[4] = 'r' ; oracle[5] = 't' ; oracle[6] = 'y' ;
    oracle[7] = 'z' ;

    set_union_t(begin(a), end(a), begin(b), end(b), inserter(r,r.end()));

    REQUIRE(r == oracle);
}