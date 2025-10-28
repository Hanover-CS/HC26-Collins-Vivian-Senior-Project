---
title: Practice & Conclusion
layout: default
---

# Quick Links
1. [Homepage](./tutorial.md)
2. [Getting Started](./gettingStarted.md)
3. [Project Strucure](./projectStructure.md)
4. [Project Setup](./setup.md)
5. Practice *(current)*
6. [Homepage](../index.md)
7. [Proposal](../proposal.md)

---

## Practice Problems

### 1. Understanding the `expect` and `actual` key terms

    Accomplish:

- understanding how to define platform-specific code using an example
- how shared implementations can work together

**Step 1:** In `commonMain`, declare function:

`expect fun getPlatformName(): String`

**Step 2:** In `androidMain` and `iosMain`, implement function:

```kotlin
// androidMain
actual fun getPlatformName(): String = "Android"

// iosMain
actual fun getPlatformName(): String = "iOS"
```

**Step 3:** In the shared module, create a `greeting()` function that would use `getPlatform()`

```kotlin
fun greeting(): String = "Hello from ${getPlatformName()}"
```

**Step 4:** Try to call `greeting()` from both Android and iOS apps, and then print the final result.

### 2. Adding a Platform-Specific Date Function

    Accomplish:
    - an understanding of how to use platform libraries!

**Step 1:** In `commonMain`, define:

```kotlin
expect fun getCurrentDate(): String
```

**Step 2:** Implement it in each platform module:
```kotlin
// androidMain
actual fun getCurrentDate(): String = java.time.LocalDate.now().toString()

// iosMain
actual fun getCurrentDate(): String {
    val formatter = NSDateFormatter()
    formatter.dateFormat = "yyyy-MM-dd"
    return formatter.stringFromDate(NSDate())
}
```

**Step 3:** Finally, call this from both the Android and iOS apps and ensure that the correct date prints.

---

## Conclusion

In summary, being able to utilize KMP to its full extent is filled with a lot of great features that aren't super hard to learn. We went over serveral topics, such as:

- how to set up KMP, even on Windows 
- specifics on how KMP is structured
- different aspects of default code and what they mean/how they contribute to the app's creation
- shared code integration in the apps

With all this in mind, it is a lot easier to get started with the app creation.

#### Need extra help? The [main tutorial page](./tutorial.md) has additional resources to use!

---

### [<- Back to Setup](./setup.md)

---

### [<- Back to Home](../index.md)