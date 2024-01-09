English | [简体中文](README_zh.md)
# Introduction to the Features of the Mental Health Assistant App

## Possible Improvements
- For the background color of the diary this kind of light beige
- And for users full journal experience
- Put lines like a notebook and I highly suggest that the font in the diary part is Montserrat fontsyle.
- The bottle is designed in the form of a message-in-a-bottle.
- The to-do list is designed like a memo pad or notebook, with a layout similar to a diary.
- put check boxes in the plan list
- Remove reward word use Calendar Progress instead
- Put pastel design background in the reward activity
- Add cute background images to the little girl's interface.
- Change the names of the bottom buttons for the diary and the schedule to 'Click me!',and change the color of the buttons to pink

## Table of Contents
1. [Main Interface (MainActivity)](#main-interface-mainactivity)
2. [Planning Activity (PlanActivity)](#planning-activity-planactivity)
3. [Bottle Activity (BottleActivity)](#bottle-activity-bottleactivity)
4. [Diary Activity (DiaryActivity)](#diary-activity-diaryactivity)
5. [Reward Activity (RewardActivity)](#reward-activity-rewardactivity)
6. [Contact Interface (ContactActivity)](#contact-interface-contactactivity)
7. [Crying Girl Interface (CryingGirlActivity)](#crying-girl-interface-cryinggirlactivity)
8. [Startup Interface (SplashActivity)](#startup-interface-splashactivity)

### Main Interface (MainActivity)
- Contains four buttons, each leading to different functional modules.
- Provides access to the diary, planning, reward, and contact information.

### Planning Activity (PlanActivity)
- Allows users to add, edit, and view daily plans.
- Supports marking the completion status of plans through swipe actions.
- Displays encouraging animations upon plan completion.

### Bottle Activity (BottleActivity)
- Shows the total number of plans and completed plans for the current date.
- Plays different animation effects based on the number of completed plans.

### Diary Activity (DiaryActivity)
- Users can record and view their diaries.
- Supports adding and editing diary entries.
- Displays a list of all diary entries.

### Reward Activity (RewardActivity)
- Includes a calendar view that allows users to select specific dates.
- Displays diary and plan records for the selected date.
- Provides motivational feedback based on diary and plan completion.

### Contact Interface (ContactActivity)
- Provides contact information and other important details.
- Used for seeking help or providing feedback.

### Crying Girl Interface (CryingGirlActivity)
- Displays an animated girl reflecting the user's activity status.
- If the user hasn't completed any tasks for several days, a crying girl animation is shown.
- An encouragement button encourages users to complete tasks and change the girl's state.

### Startup Interface (SplashActivity)
- Shown when the app is launched.
- Displays a different startup screen based on the current theme (day/night mode).
- Includes a brief animation before transitioning to the main interface.


## Technical Architecture
1. **Frontend Interface (UI/UX)**
    - Utilizes Android's standard UI components and layouts.
    - Employs the Lottie animation library to enhance the user interaction experience.
    - Uses `RecyclerView` for displaying dynamic lists, such as plan and diary entries.

2. **Backend Logic**
    - Java serves as the primary programming language.
    - Activities (`Activity`) manage different user interfaces and interactions.
    - Utilizes gesture detectors (`GestureDetector`) to handle complex user inputs, such as double-taps and swipe actions.

3. **Data Storage**
    - Utilizes an SQLite database for local data storage.
    - Database operations include table creation, insertion, updating, querying, and deletion.

4. **Data Models**
    - Defines data classes (`Plan`, `DiaryEntry`) to represent the core data structures within the application.

## Database Design
1. **Plans Table**
    - `id`: Primary key, auto-incremented.
    - `plan`: Text, representing plan content.
    - `completed`: Integer, indicating whether the plan is completed (0 for incomplete, 1 for completed).
    - `timestamp`: Text, recording the date of the plan.

2. **Diary Entries Table**
    - `id`: Primary key, auto-incremented.
    - `entry`: Text, representing diary content.
    - `timestamp`: Text, recording the date of the diary entry.

## Key Technical Points
- Database operations are performed using Android's `SQLiteDatabase` class.
- The `ContentValues` class is used to encapsulate data to be inserted or updated.
- Query results are traversed and processed using the `Cursor` object.
- Data persistence ensures that user data is retained even after the app is closed.
- The adapter pattern (`Adapter`) is used to connect data models with `RecyclerView`.


