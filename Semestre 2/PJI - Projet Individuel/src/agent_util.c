#include <jvmti.h>
#include <stdlib.h>
#include <string.h>

#include "agent_util.h"





/**
 * @brief Create a new java thread.
 * 
 * @param jni 
 * @return jthread 
 */
jthread
new_java_thread(JNIEnv* jni)
{
    // Get the class java/lang/Thread

    jclass class = (*jni)->FindClass(jni, "java/lang/Thread");

    if(class == NULL)
    {
        fprintf(stderr, "ERROR : Could not find \"java/lang/Thread\".\n");
        return NULL;
    }


    // Get the constructor of the class java/lang/Thread

    jmethodID constructor = (*jni)->GetMethodID(jni, class, "<init>", "()V");

    if(constructor == NULL)
    {
        fprintf(stderr, "ERROR : Could not find the constructor of the class \"java/lang/Thread\".\n");
        return NULL;
    }


    // Construct the new thread

    return (*jni)->NewObject(jni, class, constructor);
}





/**
 * @brief Return if a name is that of a JVM thread.
 * 
 * @param name The name of a thread.
 * @return int If the name provided is one of a JVM thread.
 */
int
is_a_jvm_thread(char* name)
{
    // For each JVM thread name

    for(unsigned int i=0; i<nb_jvm_theads; i++)
    {
        // Compare it with the name to look for
        // Return immediately 1 if the name is indeed one of a JVM thread

        if(strcmp(name, jvm_threads_name[i]) == 0)
            return 1;
    }


    // Return 0 if not

    return 0;
}





/**
 * @brief Add another thread name in the list of JVM thread names.
 * 
 * @param name The name to add.
 */
void
add_a_jvm_thread(char* name)
{
    // Increment the number of JVM threads
    // Realloc the size of the array
    // Insert the name in the array

    nb_jvm_theads++;

    jvm_threads_name = realloc(jvm_threads_name, sizeof(char) * MAX_LENGTH_NAME * nb_jvm_theads);
    
    jvm_threads_name[nb_jvm_theads-1] = strdup(name);
}





/**
 * @brief Return if a name is that of a monitoring thread.
 * 
 * @param name The name of a thread.
 * @return int If the name provided is one of a monitoring thread.
 */
int
is_a_monitoring_thread(char* name)
{
    // For each monitoring thread name

    for(unsigned int i=0; i<nb_threads; i++)
    {
        // Compare it with the name to look for
        // Return immediately 1 if the name is indeed one of a monitoring thread

        if(strcmp(name, monitoring_thread_names[i]) == 0)
            return 1;
    }


    // Return 0 if not

    return 0;
}