add_executable (test_stack
  test_main.cpp
  test2.cpp
  # Ajoutez d'autres tests ici
  )

target_link_libraries (test_stack PRIVATE stack)
add_test (NAME test_stack COMMAND test_stack)
