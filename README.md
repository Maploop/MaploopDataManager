# Maploop's Data Manager
This is a simple data-document management application. Mainly developped to store your passwords locally without having to worry about others accessing your files and knowing them.

| Table of Contents |  | 
|----- | -----|
| 1. [Security](#security) | 2. [Structure](#structure) |
| 3. [To-Do List](#to-do-list) | 4. [Installation](#installation) |
| 5. [Compiling](#compiling) | 6. [Contribute](#contribute) |




# Security
Maploop's Data Manager uses an encryption system with a key, called 'SHEN'. It's a self-made name! It encodes your data into Base64. **The data cannot be decoded without having the key**. It is recommended to keep this key short, as the app does not save your key anywhere and you have to enter it every time you want to open the application.

#### In Layman's terms, you must memorize your own key, so it's recommended to keep it short.

# Structure
The app uses a node system. Each node acts as a document, each document can have an infinite number of fields, (keys and values). The only limitation is you cannot have a node within a node.

# To-Do List
Sorted by priority;
- [ ]  Editing/Deleting Nodes
- [ ]  An actual respectable options modal
- [ ]  An actual respectable developer mode setting
- [ ]  Improve UX
- [ ]  Custom themes

# Installation
Here are some steps on the installation:
1. Download the `Maploop's Data Manager.zip` from [here](https://github.com/Maploop/MaploopDataManager/releases/latest).
2. Extract the contents to a safe folder, make sure the `imgui.ini` and `IntelligenceCenter-1.0-SNAPSHOT.jar` file are in the same folder.
3. Run `IntelligenceCenter-1.0-SNAPSHOT.jar` and enter your desired key. (Keep in mind this key is going to be your password to entering the app and the decryption key to your files **FOREVER**.)
4. (OPTIONAL) Create a desktop shortcut of the .jar file and add it to your start menu!

# Compiling
Open your terminal and run the following commands:
1. `git clone https://github.com/Maploop/MaploopDataManager.git`
2. `cd MaploopDataManager`
3. `mvn clean package`
And then, to run the app simply do `java -jar target/IntelligenceCenter-1.0-SNAPSHOT.jar` or just double click on the output file.

# Contribute
Pull-requests are welcome. I personally do not update this project very often, I would appericiate feedback in issues or pull requests for new features and fixes.
The current code is **very messy** and the reason is this project started off as a personal thing I would only use myself. But I'm planning to expand on it.
