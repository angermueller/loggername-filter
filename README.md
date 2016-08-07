# loggername-filter
Filter log entries based on their loggers name

Especially useful if you want to see more details from your own loggers (e.g. TRACE) but not all messages from the other components.
You can set a override level to get all messages above a certain level.

## Usage
Use this filter in your logback.xml 

Here an example
```xml
<?xml version="1.0" encoding="UTF-8"?>

<configuration>
    <appender name="stdout" class="ch.qos.logback.core.ConsoleAppender">
        <Target>System.out</Target>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss} %highlight(%-5p) %c{1}:%L - %m%n</pattern>
        </encoder>
        <filter class="de.angermueller.logging.LoggerNameFilter">
            <regex>com.example.yourpackage.*</regex>
            <overrideLevel>INFO</overrideLevel>
        </filter>
    </appender>

    <root level="TRACE">
        <appender-ref ref="stdout"/>
    </root>

</configuration>
```
This will get you all events from Level INFO and above and additionally all events from loggers, 
which match the expression "com.example.yourpackage.*" (including all loggers from classes in the package com.example.yourpackage).

## Dependencies

This filter is based on ch.qos.logback logback-classic.