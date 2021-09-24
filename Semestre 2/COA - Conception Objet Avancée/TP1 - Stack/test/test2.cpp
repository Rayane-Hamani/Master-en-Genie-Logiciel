// This is not the main
#include "catch.hpp"

#include "stack.h"

// Basic test
TEST_CASE("Create a Stack and insert an element", "[stack]")
{
    Stack s;
    s.push(1);
    REQUIRE(s.size() == 1);
    s.pop();
    REQUIRE(s.size() == 0);
    REQUIRE(s.isEmpty() == true);
}

// Test that an exception is raised when we try to read from an empty
// stack
TEST_CASE("Pop an element from an empty stack", "[stack]")
{
    Stack s;
    s.push(1);
    REQUIRE(s.size() == 1);
    s.pop();    

    REQUIRE_THROWS(s.top());
}

// Test that the element just inserted is always at the top
TEST_CASE("Push and read top", "[stack]")
{
    Stack s;

    for (int i=0; i<1000; i++) {
        s.push(i);
        REQUIRE(s.top() == i);
    }   
}

// Elements are extracted in inverse order or insertion
TEST_CASE("Inverted read", "[stack]")
{
    Stack s;

    for (int i = 0; i<1000; i++)
        s.push(i);

    for (int i=999; i>=0; i--) {
        REQUIRE(s.top() == i);
        s.pop();
    }
}

// Test that the size is always equal to the number of elements inside
TEST_CASE("Size", "[stack]")
{
    Stack s;

    for (int i=0; i<1000; i++) {
        s.push(i);
        REQUIRE(s.size() == i+1);
    }
}


// Test that maxsize is always greater than size
TEST_CASE("MaxSize", "[stack]")
{
    Stack s;

    for (int i=0; i<1000; i++) {
        s.push(i);
        REQUIRE(s.size() <= s.maxsize());
    }

    for (int i=999; i>=0; i--) {
        s.pop();
        REQUIRE(s.size() <= s.maxsize());
        REQUIRE(i <= s.size());
    }
}

// Test that copies a stack and checks both are identical
TEST_CASE("Test du Copy Constructor","[stack]")
{
    Stack s;
    s.push(1);
    s.push(2);
    s.push(3976);
    Stack s2(s);
    REQUIRE(s.size() == s2.size());
    REQUIRE(s.maxsize() == s2.maxsize());
    REQUIRE(s == s2);
}

// Tests that a stack ups its size when hitting it's limit
TEST_CASE("Test of increasing size","[stack]")
{
    Stack s;
    REQUIRE(s.maxsize() == 20);
    for(int i = 0; i < 30; i++){s.push(i);}
    REQUIRE(s.maxsize() == 40);
}

// Tests that a stack gives it's value in reversed order from the order they were inserted.
TEST_CASE("Test of inverted values","[stack]")
{
    Stack s;
    for(int i = 0; i < 30; i++){s.push(i);}
    for(int i = 29; i >= 0; i--){
        REQUIRE(s.top() == i);
        s.pop();
    }
}

// Test of the = operator
TEST_CASE("Test of =","[stack]")
{
    Stack s1;
    Stack s2;
    Stack s3;
    Stack s4;

    for(int i = 0; i < 77; i++){
        s1.push(i);
        s2.push(i);
    }

    for(int i = 0; i < 200; i += 2){s3.push(i);}

    s1 = s2;
    REQUIRE(s1 == s2);

    s1 = s3;
    REQUIRE(s1 == s3);

    s4 = s1;
    REQUIRE(s1 == s4);
}

// Test of reduce
TEST_CASE("Test of reduce()","[stack]")
{
    Stack s;
    for(int i = 0; i < 15; i++){s.push(i);}
    s.reduce();
    REQUIRE(s.size() == s.maxsize());
    REQUIRE(s.maxsize() == 15);
}

// Test of safe push
TEST_CASE("Test of safe push","[stack]")
{
    Stack s;
    for(int i = 15; i < 9; i--){
        s.push(i);
    }
    s += 9;
    REQUIRE(s.top() == 9);
    REQUIRE_THROWS(s += 15);
}