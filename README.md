<h1 align="center" id="title">User Activity Monitor</h1>

<p align="center"><img src="https://socialify.git.ci/er-dhimanabhishek/UserActivityMonitor/image?language=1&amp;owner=1&amp;name=1&amp;stargazers=1&amp;theme=Light" alt="project-image"></p>

<p id="description">For this project I have used the Usage Stats Manager to access the daily usage stats of the user's mobile device and consolidated them to show the total usage of every application. This tells the user how much time the user is spending on every single app.</p>

<h2>Pre-requisites:</h2>

*   Android Studio Flamingo | 2022.2.1
*   Gradle: 8.0.0
*   JDK 18
*   Min SDK version 23(To access Usage Stats min SDK required is 21)

<h2>Permission:</h2>

Usage Stats access permission is required:

```

<uses-permission
        android:name="android.permission.PACKAGE_USAGE_STATS"
        tools:ignore="ProtectedPermissions" />

```

Check if permission is granted or not:

```

val appOpsManager =
            context.getSystemService(APP_OPS_SERVICE) as AppOpsManager
        val mode: Int = appOpsManager.checkOpNoThrow(
            "android:get_usage_stats",
            Process.myUid(), packageName
        )
        return mode == AppOpsManager.MODE_ALLOWED

```

<h2>Query usage stats:</h2>

```

val usageStatsManager =
            ctx.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

        val usageEvents = usageStatsManager.queryEvents(beginTimeMills, endTimeMills)

```

*   beginTimeMills and endTimeMills is the time interval for which we want to query the usage stats and this time frame is specified in milli seconds 

<h2>Project Screenshots:</h2>

:-------------------------:|:-------------------------:
![]([https://...Dark.png](https://i.postimg.cc/bwd4Cz0p/Screenshot-2024-04-14-at-11-25-18-AM.png))  |  ![]([https://...Dark.png](https://i.postimg.cc/BQnzbqVH/Screenshot-2024-04-14-at-11-24-37-AM.png))  |  ![]([https://...Dark.png](https://i.postimg.cc/764skDzC/Screenshot-2024-04-14-at-11-22-28-AM.png))  |  ![]([https://...Dark.png](https://i.postimg.cc/sxN4vRM4/Screenshot-2024-04-14-at-11-23-04-AM.png))

  
  
<h2>💻 Built with</h2>

Technologies used in the project:

*   Kotlin
*   Kotlin-coroutines
*   Data Binding
*   MVVM
*   Room DB
