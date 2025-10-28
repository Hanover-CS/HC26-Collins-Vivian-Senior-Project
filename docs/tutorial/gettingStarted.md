---
title: Getting Started
layout: default
---

# Quick Links
1. [Homepage](./tutorial/tutorial.md)
2. Getting Started *(Current)*
3. [Project Strucure](./projectStructure.md)
4. [Project Setup](./setup.md)
5. [Practice](./practice.md)
6. [Homepage](./index.md)
7. [Proposal](./proposal.md)

## Basic Overview

**Important note: If using a Windows machine, having access to a Mac is completely necessary to create an iOS app.**

This tutorial is aimed at those with a basic familiarity of Android Studio and/or IntelliJ and the Kotlin programming language.

Kotlin Multiplatform is an amazing tool to create an app with code that can be reused across various other platforms. By structuring the project for code sharing, you reduce duplication and redundant code for core features.

## Getting Started with Kotlin Multiplatform (KMP)
There are two different ways to utilize KMP:

### Option 1: Android Studio:

Follow [this tutorial](https://developer.android.com/studio/install) to successfully install Android Studio. This tutorial includes both Windows, as well as Mac. 

### Option 2: IntelliJ:
Follow [this link](https://www.jetbrains.com/help/idea/installation-guide.html) to successfully install IntelliJ. This link includes both Windows, as well as Mac. It is important to note that an account is required to utilize IntelliJ's features.

Once installed in IntelliJ:
1. Choose New Project → Kotlin → Multiplatform
2. Select the desired targets (Android, iOS, JVM, JS, or Native)
3. Configure your Gradle settings and create the project

Additionally, a JetBrains account is required to access IntelliJ’s full KMP features.

---

## Configuring the project in Android Studio
**If on Windows:**
You **cannot** directly create a KMP App through Android Studio or IntelliJ. You can create the project structure by visiting the [Kotlin Multiplatform Wizard](https://kmp.jetbrains.com/?android=true&ios=true&iosui=compose&includeTests=true). From there:

1. Name your project
2. Set the ID
3. Choose the target platform
    - Android
    - iOS
    - Desktop
    - Web
    - Server
4. Download the file
5. Extract the files
6. Open it in IntelliJ or Android Studio

Next, we will move on to the KMP's project structure to understand how it works and what it should look like. 

---

### [-> Project Structure](./projectStructure.md)

---

### [<- Back to Beginning](./tutorial.md)

---

### [<- Back to Home](./index.md)
