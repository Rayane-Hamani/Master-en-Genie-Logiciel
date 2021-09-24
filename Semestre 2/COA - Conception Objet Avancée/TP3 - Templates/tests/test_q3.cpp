#include "catch.hpp"
#include "myclass.hpp"

#include <iostream>

#include <vector>
#include <algorithm>
#include "set_functions.hpp"

#include <list>

using namespace std;

TEST_CASE("intersectionMyClass", "[Q3]"){

    vector<MyClass> v1 = {1,2,3,4,5,6,7};
    vector<MyClass> v2 = {5,6,7,8,9,10};
    vector<MyClass> r;
    vector<MyClass> oracle = {5,6,7};

    set_intersection_t(begin(v1), end(v1), begin(v2), end(v2), back_inserter(r));

    REQUIRE(r == oracle);
}

TEST_CASE("unionMyClass", "[Q3]"){

    vector<MyClass> v1 = {1,2,3,4,5,6,7};
    vector<MyClass> v2 = {5,6,7,8,9,10};
    vector<MyClass> r;
    vector<MyClass> oracle = {1,2,3,4,5,6,7,8,9,10};

    set_union_t(begin(v1), end(v1), begin(v2), end(v2), back_inserter(r));

    REQUIRE(r == oracle);
}

TEST_CASE("intersectionMyClassVecteurVide", "[Q3]"){

    vector<MyClass> v1 = {1,2,3,4,5,6,7};
    vector<MyClass> v2 = {};
    vector<MyClass> r;
    vector<MyClass> oracle = {};

    set_intersection_t(begin(v1), end(v1), begin(v2), end(v2), back_inserter(r));

    REQUIRE(r == oracle);
}

TEST_CASE("intersectionMyClassVecteursVide", "[Q3]"){

    vector<MyClass> v1 = {};
    vector<MyClass> v2 = {};
    vector<MyClass> r;
    vector<MyClass> oracle = {};

    set_intersection_t(begin(v1), end(v1), begin(v2), end(v2), back_inserter(r));

    REQUIRE(r == oracle);
}

TEST_CASE("unionMyClassVecteurVide", "[Q3]"){

    vector<MyClass> v1 = {1,2,3,4,5};
    vector<MyClass> v2 = {};
    vector<MyClass> r;
    vector<MyClass> oracle = {1,2,3,4,5};

    set_union_t(begin(v1), end(v1), begin(v2), end(v2), back_inserter(r));

    REQUIRE(r == oracle);
}

TEST_CASE("unionMyClassVecteursVide", "[Q3]"){

    vector<MyClass> v1 = {};
    vector<MyClass> v2 = {};
    vector<MyClass> r;
    vector<MyClass> oracle = {};

    set_union_t(begin(v1), end(v1), begin(v2), end(v2), back_inserter(r));

    REQUIRE(r == oracle);
}