cmake_minimum_required(VERSION 3.1O)

add_library(stack stack.cpp)
target_include_directories(stack PUBLIC ${PROJECT_SOURCE_DIR}/include)
target_compile_features(stack PUBLIC cxx_std_14)

add_executable(main main.cpp)
target_link_libraries(main PRIVATE stack)
target_compile_features(main PUBLIC cxx_std_14)
