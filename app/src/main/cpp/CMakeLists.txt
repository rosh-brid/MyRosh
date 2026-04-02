cmake_minimum_required(VERSION 3.22.1)

project("myrosh")

add_library(
    deviceinfo
    SHARED
    deviceinfo.cpp
)

find_library(
    log-lib
    log
)

target_link_libraries(
    deviceinfo
    ${log-lib}
)
