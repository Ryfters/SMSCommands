package com.ryfter.smscommands.shizuku;

// Declare any non-default types here with import statements

interface IShizukuService {
    void destroy() = 16777114; // Destroy method defined by Shizuku server

    void exit() = 1; // Exit method defined by user

    void logUserInfo() = 2;
}