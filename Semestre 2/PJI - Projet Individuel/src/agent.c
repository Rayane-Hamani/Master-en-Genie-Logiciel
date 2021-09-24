#include <jvmti.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

#include "agent_util.h"
#include "cpu_usage.h"





void JNICALL
Callback_ThreadStart(jvmtiEnv* jvmti, JNIEnv* jni, jthread thread)
{
    jvmtiThreadInfo thread_info;

    jvmtiError error = (*jvmti)->GetThreadInfo(jvmti, thread, &thread_info);

    if(error != JVMTI_ERROR_NONE)
    {
        fprintf(stderr, "ERROR %d : Could not get the thread infos.\n", error);
        return;
    }


    // Check if the thread started is a jvm or monitoring thread and if not

    if(!is_a_jvm_thread(thread_info.name) && !is_a_monitoring_thread(thread_info.name))
    {
        // Create a monitoring thread

        jthread monitoring_thread = new_java_thread(jni);

        if(monitoring_thread == NULL)
        {
            fprintf(stderr, "ERROR : Could not create the monitoring thread.\n");
            return;
        }


        // Store the names of the thread and its monitoring thread

        jvmtiThreadInfo monitoring_thread_info;

        error = (*jvmti)->GetThreadInfo(jvmti, monitoring_thread, &monitoring_thread_info);

        if(error != JVMTI_ERROR_NONE)
        {
            fprintf(stderr, "ERROR %d : Could not get the thread infos.\n", error);
            return;
        }

        nb_threads++;

        thread_names            = realloc(thread_names           , sizeof(char) * MAX_LENGTH_NAME * nb_threads);
        monitoring_thread_names = realloc(monitoring_thread_names, sizeof(char) * MAX_LENGTH_NAME * nb_threads);        
        
        thread_names[nb_threads-1]            = strdup(thread_info.name);
        monitoring_thread_names[nb_threads-1] = strdup(monitoring_thread_info.name);


        // Free the memory allocated for the monitoring thread name

        (*jvmti)->Deallocate(jvmti, (void *) monitoring_thread_info.name);


        // Run the monitoring agent with the thread name (char*) as argument

        (*jvmti)->RunAgentThread(jvmti, monitoring_thread, &run, thread_names[nb_threads-1], JVMTI_THREAD_MIN_PRIORITY);
    }

    
    // Free the memory allocated for the thread name

    (*jvmti)->Deallocate(jvmti, (void *) thread_info.name);
}





void JNICALL
Callback_ThreadEnd(jvmtiEnv* jvmti, JNIEnv* jni, jthread thread)
{
    // Get the name of the thread

    jvmtiThreadInfo thread_info;

    jvmtiError error = (*jvmti)->GetThreadInfo(jvmti, thread, &thread_info);

    if(error != JVMTI_ERROR_NONE)
    {
        fprintf(stderr, "ERROR %d : Could not get the thread infos.\n", error);
        return;
    }


    // Check if the thread started is a jvm or monitoring thread and if not

    if(!is_a_jvm_thread(thread_info.name) && !is_a_monitoring_thread(thread_info.name))
    {
        // Get its total cpu time or return if it is not possible anymore

        jlong cpu_time;

        error = (*jvmti)->GetThreadCpuTime(jvmti, thread, &cpu_time);

        if(error != JVMTI_ERROR_NONE)
        {
            fprintf(stderr, "ERROR %d : Could not get the cpu time of %s.\n", error, thread_info.name);
            (*jvmti)->Deallocate(jvmti, (void *) thread_info.name);
            return;
        }


        // Print the total cpu time of the thread

        printf("%-9s   |   cpu_time : %4ld ms   <-END\n", thread_info.name, TIME_IN_MS(cpu_time));
    }


    // Free the memory allocated for the thread name
    
    (*jvmti)->Deallocate(jvmti, (void *) thread_info.name);
}





JNIEXPORT jint JNICALL
Agent_OnLoad(JavaVM* jvm, char* options, void* reserved) 
{
    jvmtiEnv* jvmti = NULL;


    // Get the JVMTI environment

    jvmtiError error = (*jvm)->GetEnv(jvm, (void **)&jvmti, JVMTI_VERSION);
    
    if(error != JNI_OK)
    {
        fprintf(stderr, "ERROR %d : Could not get the environment.\n", error);
        return JNI_ERR;
    }


    // Enable the capabilities for the JVMTI methods

    jvmtiCapabilities capabilities;
    
    memset(&capabilities, 0, sizeof(jvmtiCapabilities));
    
    capabilities.can_get_thread_cpu_time = 1;
    capabilities.can_signal_thread       = 1;

    error = (*jvmti)->AddCapabilities(jvmti, &capabilities);
    
    if(error != JVMTI_ERROR_NONE)
    {
        fprintf(stderr, "ERROR %d : Could not add capabilities.\n", error);
        return error;
    }


    // Enable the event callbacks

    jvmtiEventCallbacks callbacks;
    
    callbacks.ThreadStart = &Callback_ThreadStart;
    callbacks.ThreadEnd   = &Callback_ThreadEnd;
    
    error = (*jvmti)->SetEventCallbacks(jvmti, &callbacks, (jint)sizeof(callbacks));
    
    if(error != JVMTI_ERROR_NONE)
    {
        fprintf(stderr, "ERROR %d : Could not set event callbacks.\n", error);
        return error;
    }


    // Enable the event notifications

    jvmtiEvent events[] =
    {
        JVMTI_EVENT_THREAD_START, JVMTI_EVENT_THREAD_END
    };

    for(unsigned int i=0; i<sizeof(events)/sizeof(jvmtiEvent); i++)
    {
        error = (*jvmti)->SetEventNotificationMode(jvmti, JVMTI_ENABLE, events[i], (jthread)NULL);
    
        if(error != JVMTI_ERROR_NONE)
        {
            fprintf(stderr, "ERROR %d : Could not set event notification mode.\n", error);
            return error;
        }
    }


    // Initialize global variables

    nb_threads = 0;
    nb_jvm_theads = 0;

    add_a_jvm_thread("Reference Handler");
    add_a_jvm_thread("Finalizer");
    add_a_jvm_thread("Signal Dispatcher");
    add_a_jvm_thread("Notification Thread");
    add_a_jvm_thread("Common-Cleaner");


    // Return JNI_OK to signify success

    return JNI_OK;
}