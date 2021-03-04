# wallet
Digital Wallet Assignment
Brief
This is a very simple wallet function. The user is expected to implement a springboot implementation connected to a H2 DB with RESTful endpoints that can perform the following:
• Take in a set of discrete integers each representing a discrete cash through a REST call and saved them into a Database (H2);
• Through a call to the REST endpoint, print out the set of discrete integers currently in the Database; and
• Through a call to the REST endpoint, pay a value (integer) (and updating the discrete cash in the Database)

Other considerations
• In your code, you should be able to demonstrate use of Unit Tests in consideration of different edge cases;
• You do need to worry about replenishment of coins;
• You have to implement in Java 8 (and above); and
• You can use any IDE of your choice for the implementation.

Test Cases to Fulfil (Required to Demo) User What should happen Output
Initialize the db with a set of coins 2, 3, 1, 2, 1 through a call to the REST endpoint
Springboot persist the coins in the h2 DB

Success
The user reads the contents of the wallet from springboot through a call to the REST endpoint
Coins are re-ordered and output the contents of the wallet
My current coins are [1, 1, 2, 2, 3]
Pay with an exact without change: 1. Pay(1)
Success and output the contents of the wallet
Successfully paid 1
My current coins are [1, 2, 2, 3]
Pay with exact without change:
1. Pay(3)
Success and output the contents of the wallet
Successfully paid 3
My current coins are [2, 3]
Pay with change:
1. Pay using an amount and you add the change back to the list -> Pay(1)
Success and output the contents of the wallet
Successfully paid 1
My current coins are [1, 3]
Pay with change:
1. Pay using an amount and you add the change back to the list -> Pay(2)
Success and output the contents of the wallet
Successfully paid 2
My current coins are [2]
Pay with more than what you have in your wallet -> Pay(5)
Failure and an appropriate error is thrown
You do not have sufficient coins to pay 5.
My current coins are [2]
