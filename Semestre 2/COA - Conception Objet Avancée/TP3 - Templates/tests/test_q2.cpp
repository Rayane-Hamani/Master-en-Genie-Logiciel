#include "catch.hpp"

#include <iostream>

#include <vector>
#include <algorithm>
#include "set_functions.hpp"

#include <list>

using namespace std;


// VECTOR STRING

TEST_CASE("intersectionTV", "[Q2]"){

    vector<string> v1 = {"a","b","c","d","e","f","g"};
    vector<string> v2 = {"e","f","g","h","i","j"};
    vector<string> r;
    vector<string> oracle = {"e","f","g"};

    set_intersection_t(begin(v1), end(v1), begin(v2), end(v2), back_inserter(r));

    REQUIRE(r == oracle);
}

TEST_CASE("unionTV", "[Q2]")
{
    vector<string> v1 = {"a","b","c","d","e","f","g"};
    vector<string> v2 = {"e","f","g","h","i","j"};
    vector<string> r;
    vector<string> oracle = {"a","b","c","d","e","f","g","h","i","j"};

    set_union_t(begin(v1), end(v1), begin(v2), end(v2), back_inserter(r));
    sort(begin(r), end(r));
    
    REQUIRE(r == oracle);
}

TEST_CASE("intersectionTVResultatVide","[Q1]"){
    vector<string> v1 = {"a","b","c","d","e","f","g"};
    vector<string> v2 = {"h","i","j"};
    vector<string> r;
    vector<string> oracle = {};
    
    set_intersection_t(begin(v1), end(v1), begin(v2), end(v2), back_inserter(r));

    REQUIRE(r == oracle);
}

TEST_CASE("intersectionTVVecteurVide","[Q2]"){
    vector<string> v1 = {"a","b","c","d","e","f","g"};
    vector<string> v2 = {};
    vector<string> r;
    vector<string> oracle = {};
    
    set_intersection_t(begin(v1), end(v1), begin(v2), end(v2), back_inserter(r));

    REQUIRE(r == oracle);
}

TEST_CASE("intersectionTVVecteursVide","[Q1]"){
    vector<string> v1 = {};
    vector<string> v2 = {};
    vector<string> r;
    vector<string> oracle = {};
    
    set_intersection_t(begin(v1), end(v1), begin(v2), end(v2), back_inserter(r));

    REQUIRE(r == oracle);
}

TEST_CASE("unionTVVecteurVide","[Q1]"){
    vector<string> v1 = {"a","b","c","d","e","f","g"};
    vector<string> v2 = {};
    vector<string> r;
    vector<string> oracle = {"a","b","c","d","e","f","g"};
    
    set_union_t(begin(v1), end(v1), begin(v2), end(v2), back_inserter(r));

    REQUIRE(r == oracle);
}

TEST_CASE("unionTVVecteursVide","[Q1]"){
    vector<string> v1 = {};
    vector<string> v2 = {};
    vector<string> r;
    vector<string> oracle = {};
    
    set_union_t(begin(v1), end(v1), begin(v2), end(v2), back_inserter(r));

    REQUIRE(r == oracle);
}


// LIST INT
TEST_CASE("intersectionTL", "[Q2]"){

    list<int> v1 = {1,2,3,4,5,6,7};
    list<int> v2 = {5, 6, 7, 8, 9, 10};
    list<int> r;
    list<int> oracle = {5, 6, 7};

    set_intersection_t(begin(v1), end(v1), begin(v2), end(v2), back_inserter(r));

    REQUIRE(r == oracle);
}

TEST_CASE("unionTL", "[Q2]")
{
    list<int> v1 = {1,2,3,4,5,6,7};
    list<int> v2 = {5, 6, 7, 8, 9, 10};
    list<int> r;
    list<int> oracle = {1,2,3,4,5,6,7,8,9,10};

    set_union_t(begin(v1), end(v1), begin(v2), end(v2), back_inserter(r));
    
    REQUIRE(r == oracle);
}

TEST_CASE("intersectionTLResultatVide","[Q2]"){
    list<int> v1 = {1,2,3,4,5};
    list<int> v2 = {6,7,8,9,10};
    list<int> r;
    list<int> oracle = {};
    
    set_intersection_t(begin(v1), end(v1), begin(v2), end(v2), back_inserter(r));

    REQUIRE(r == oracle);
}

TEST_CASE("intersectionTLListeVide","[Q2]"){
    list<int> v1 = {1,2,3,4,5};
    list<int> v2 = {};
    list<int> r;
    list<int> oracle = {};
    
    set_intersection_t(begin(v1), end(v1), begin(v2), end(v2), back_inserter(r));

    REQUIRE(r == oracle);
}

TEST_CASE("intersectionTLListesVide","[Q2]"){
    list<int> v1 = {};
    list<int> v2 = {};
    list<int> r;
    list<int> oracle = {};
    
    set_intersection_t(begin(v1), end(v1), begin(v2), end(v2), back_inserter(r));

    REQUIRE(r == oracle);
}

TEST_CASE("unionTLListeVide","[Q2]"){
    list<int> v1 = {1,2,3,4,5};
    list<int> v2 = {};
    list<int> r;
    list<int> oracle = {1,2,3,4,5};
    
    set_union_t(begin(v1), end(v1), begin(v2), end(v2), back_inserter(r));

    REQUIRE(r == oracle);
}

TEST_CASE("unionTLListesVide","[Q2]"){
    list<int> v1 = {};
    list<int> v2 = {};
    list<int> r;
    list<int> oracle = {};
    
    set_union_t(begin(v1), end(v1), begin(v2), end(v2), back_inserter(r));

    REQUIRE(r == oracle);
}