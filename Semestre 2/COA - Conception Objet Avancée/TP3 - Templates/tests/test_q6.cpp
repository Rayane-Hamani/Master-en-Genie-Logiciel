#include "catch.hpp"
#include "myclass.hpp"

#include <iostream>

#include <vector>
#include <algorithm>
#include "set_functions.hpp"

#include <list>

#include <map>

using namespace std;

TEST_CASE("intersectGeneral","[Q6]"){
    map<int, string> a, b;
    vector<int> v = {1, 3, 5};

    a[1] = "A"; a[2] = "B"; a[3] = "C"; a[4] = "D";

    set_intersection_t(begin(a), end(a), begin(v), end(v), inserter(b, b.end()), compare); 

// b should contain (1, "A") and (3, "C") and nothing else. 
}