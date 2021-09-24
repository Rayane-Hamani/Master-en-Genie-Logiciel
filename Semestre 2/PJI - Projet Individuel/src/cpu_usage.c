#include <jvmti.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

#include "cpu_usage.h"





#define TIME_TO_SLEEP 1 // in seconds





/**
 * @brief Find a thread by its name.
 * 
 * @param jvmti The JVMTI environment.
 * @param name The name of the thread to look for.
 * 
 * @return jthread The thread found.
 */
jthread
findThreadByName(jvmtiEnv* jvmti, char* name)
{
    jint nb_threads;
    jthread* threads;


    // Get all the threads alive

    jvmtiError error = (*jvmti)->GetAllThreads(jvmti, &nb_threads, &threads);

    if(error != JVMTI_ERROR_NONE)
    {
        fprintf(stderr, "ERROR %d : Could not get all threads while looking for %s.\n", error, name);
        return NULL;
    }
    

    // For each thread alive

    for(int i=0; i<nb_threads; i++)
    {
        // Get the name of the thread

        jvmtiThreadInfo thread_info;

        error = (*jvmti)->GetThreadInfo(jvmti, threads[i], &thread_info);

        if(error != JVMTI_ERROR_NONE)
            continue;


        // Compare it with the name to look for
        // Free the memory allocated for the thread name and return immediately the thread if the comparison matches

        if(strcmp(name, thread_info.name) == 0)
        {
            (*jvmti)->Deallocate(jvmti, (void *) thread_info.name);

            return threads[i];
        }


        // Free the memory allocated for the thread name

        (*jvmti)->Deallocate(jvmti, (void *) thread_info.name);
    }

    return NULL;
}





/**
 * @brief Agent that will print the cpu usage.
 * 
 * @param jvmti The JVMTI environment.
 * @param jni The JNI environment.
 * @param data Data useful to the thread (here it is the name of the thread monitored by this agent).
 */
void
run(jvmtiEnv* jvmti, JNIEnv* jni, void* data)
{
    // Retrieve the monitored thread by its name or end the agent

    char* name = data;

    jthread thread = findThreadByName(jvmti, name);

    if(thread == NULL)
    {
        fprintf(stderr, "ERROR : Could not find a thread with the name %s.\n", name);
        free(name);
        return;
    }
    

    // While it is still possible to get the monitored thread CPU time

    jlong last_but_one_cpu_time = 0;

    while(1)
    {
        // Get the last cpu time or end the agent

        jlong last_cpu_time;

        jvmtiError error = (*jvmti)->GetThreadCpuTime(jvmti, thread, &last_cpu_time);
        
        if(error != JVMTI_ERROR_NONE)
        {
            free(name);
            return;
        }


        // Calcul the CPU usage flat and percentage

        jlong cpu_usage      = last_cpu_time - last_but_one_cpu_time;
        jlong cpu_usage_perc = (cpu_usage * 100) / (TIME_TO_SLEEP * 1000000000);

        printf("%-9s   |   cpu_time : %4ld ms   (%2ld%%)\n", name, TIME_IN_MS(cpu_usage), cpu_usage_perc);


        // Swap the last and last_but_one for the next loop
        // Sleep after

        last_but_one_cpu_time = last_cpu_time;
        
        sleep(TIME_TO_SLEEP);
    }
}