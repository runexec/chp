(defn operating-status [handler]
  (fn [req]
    (let [r (Runtime/getRuntime)
          os (java.lang.management.ManagementFactory/getOperatingSystemMXBean)
          rt (java.lang.management.ManagementFactory/getRuntimeMXBean)
          t (java.lang.management.ManagementFactory/getThreadMXBean)]          
      (println (apply str (repeat 10 "-=")))
      (doseq [stat {:ram-max (.. r maxMemory)
                    :ram-usage (.. r totalMemory)
                    :ram-free (.. r freeMemory)
                    :available-procs (.. r availableProcessors)
                    :system-load-average (.. os getSystemLoadAverage)
                    :jvm-uptime-ms (.. rt getUptime)
                    :thread-count (.. t getThreadCount)}]
        (println stat))
      (println (apply str (repeat 10 "-=")))
    (handler req))))

          
      
