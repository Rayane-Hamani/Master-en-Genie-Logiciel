#pragma once

#include <jvmti.h>





#define MAX_LENGTH_NAME 32





char** jvm_threads_name;
unsigned int nb_jvm_theads;

char** thread_names;
char** monitoring_thread_names;
unsigned int nb_threads;





jthread
new_java_thread(JNIEnv* jni);

void
add_a_jvm_thread(char* name);

int
is_a_jvm_thread(char* name);

int
is_a_monitoring_thread(char* name);

jthread
new_java_thread(JNIEnv* jni);