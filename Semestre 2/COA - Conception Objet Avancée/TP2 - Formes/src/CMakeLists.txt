cmake_minimum_required(VERSION 3.1O)

add_executable(exemple_main main.cpp)
target_include_directories(exemple_main PUBLIC ${PROJECT_SOURCE_DIR}/include)
target_compile_features(exemple_main PUBLIC cxx_std_14)

add_executable(shape_main shape_main.cpp shape.cpp rect.cpp circle.cpp triangle.cpp ShapeGroup.cpp)
target_include_directories(shape_main PUBLIC ${PROJECT_SOURCE_DIR}/include)
target_compile_features(shape_main PUBLIC cxx_std_14)

if (WIN32)
  # librairies for Windows
else()
  # librairies sur Linux
  target_link_libraries(exemple_main PRIVATE m pthread X11)
  target_link_libraries(shape_main PRIVATE m pthread X11)
endif()
