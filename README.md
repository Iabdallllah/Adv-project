Contributors:
Abdallah Ahmed
Yousef Ibrahim
Fahmy Ibrahim
Mariam Hussien
Manar Mohamed
Hana Farid
Nour Mahmoud

📚 Library System

A simple Java-based Library Management System that allows adding, removing, searching, and sorting books, along with managing library members.

📋 Features

Add new books to the library.

Remove books (only if available).

Add new members.

Search for books by title (case-insensitive).

Sort books alphabetically by title.

Exception handling for invalid operations.

🛠️ Technologies Used

Java (Core Java Concepts)

Collections Framework (ArrayList, Collections.sort)

Exception Handling

OOP Concepts (Classes, Objects, Methods)

🏛️ Class Structure

LibrarySystem
Manages the collection of Book and Member objects.

Methods:

addBook(Book book)

removeBook(Book book)

addMember(Member member)

searchBook(String title)

sortBooksByTitle()

getBooks()

getMembers()

Book
(Assumed structure)

Attributes:

String title

String author

boolean available

Methods:

getTitle()

isAvailable()

Other getters/setters

Member
(Assumed structure)

Attributes:

String name

int memberId

Methods:

getName()

getMemberId()

Other getters/setters



