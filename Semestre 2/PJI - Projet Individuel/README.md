# [Analysis of the energy consumption of a JVM](https://www.cristal.univ-lille.fr/miny/M2Info/2020-2021/PFE/afficherSujet.php?sujet=1610522307)

Par <u>**Rayane Hamani**</u>

The purpose of this project is to give an insight of Java programs's load on the CPU.
To do so, this agent will analyze the CPU usage by each thread created by the program. Thus, the agent won't look for threads created by the JVM such as Notification Thread, Common-Cleaner, etc...
The project will use JVMTI to get data from the JVM itself.

The agent is a very good complement to one that can display the call stack of the threads. It was originally the second goal of this agent but only the part about CPU usage works for the moment. With the CPU usage of each thread and their call stack, it is possible to figure out which method and which part of a code can be optimized.

## <u>**Building**</u>

The agent uses [JVMTI 15](https://docs.oracle.com/en/java/javase/15/docs/specs/jvmti.html). So it will need [JDK 15](https://www.oracle.com/java/technologies/javase/jdk15-archive-downloads.html).

The header and source files are in the folder [src](./src).

Simply use the following command to build the agent. It will be generated at the root of the project.

```bash
make
```

## <u>**Running**</u>

When launching the JVM, add `-agentpath:` followed by the relative path of the agent.

For example :

```bash
java -agentpath:./agent.so -jar jars/dacapo.jar xalan > results.txt
```

The agent's results are intended to be reviewed so it's best to redirect them in a file. A future upgrade will be to write them in a file rather than the standard output.

## <u>**Code samples**</u>

```c
void JNICALL
Callback_ThreadStart(jvmtiEnv* jvmti, JNIEnv* jni, jthread thread)
{
    jvmtiThreadInfo thread_info;

    (*jvmti)->GetThreadInfo(jvmti, thread, &thread_info);


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

        ...

        // Run the monitoring agent with the thread name (char*) as argument

        (*jvmti)->RunAgentThread(jvmti, monitoring_thread, &run, thread_names[nb_threads-1], JVMTI_THREAD_MIN_PRIORITY);
    }

    ...
}
```

When a monitoring thread is created, the JVM will send a `JVMTI_EVENT_THREAD_START` notification to the agent, thus calling the `ThreadStart` callback method. To avoid looping indefinitely, the agent distinguishes the JVM threads from monitoring threads and will only enter this loop if the started thread has been created by the program to monitor.

```c
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
```

Unfortunately, the `jthread argument` of the ThreadStart callback method is a local (temporary) variable. It does not remain allocated and is not transmitted as intended by the function `RunAgentThread`. Because the type `jthread` is a type `jobject` also being a type `struct _jobject`, it is not possible de compare it like a string (char*). To get around this, the agent has to be more creative by `getting and storing the name of the desired thread` and then `looking at all the threads alive` to compare their names and then `return the jthread object` associated with the desired name.

It is possible for the monitored program to change the name of its threads but it is very unlikely and would serve no purpose to do so. This work around is due to JVMTI's limitations but clearly does the job.

## <u>**Performance comparisons**</u>

One of the constraint of the agent is to have a limited impact on the program it monitors. It is translated in the agent by avoiding as possible notifications from the JVM.

The following results are made from 10 attempts with and 10 attempts without the agent.

DaCapo's xalan benchmark :

- With : TODO
- Without : TODO

Renaissance's benchmark :

- With : TODO
- Without : TODO

System configuration to replicate the results :

- `System OS :` Kubuntu 20.04
- `CPU :` AMD Ryzen 5 3500U
- `RAM :` 8 Go 2400 MHz (don't know the timings here)
