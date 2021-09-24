#pragma once



#include <jvmti.h>





#define TIME_IN_MS(time_in_ns)          (time_in_ns / 1000000)





jthread
findThreadByName(jvmtiEnv* jvmti, char* name);

void
run(jvmtiEnv* jvmti, JNIEnv* jni, void* data);