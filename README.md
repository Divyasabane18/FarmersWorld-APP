🌾 FARMER'S WORLD

Farmers World is an Android application that bridges the gap between farmers and customers.
Farmers can upload their fresh produce, while customers can browse, add to cart, and buy directly from local farmers — helping build a stronger, sustainable local economy. 🌱

📱 Features
👨‍🌾 Farmer Side:
🚜 Farmer Registration (with Aadhar authentication)

🖼️ Product Upload (Image, Name, Description, Price, City)

📦 View Uploaded Products

✅ Phone Number OTP Verification (for secure farmer onboarding)

🛒 Customer Side:
👩‍🌾 Customer Registration and Login

🛒 Browse Products by City (location-based filtering)

➕ Add Products to Cart

🔢 Update Quantity in Cart

🛍️ View Total Price Before Buying

✅ Proceed to Buy with Confirmation Success Dialog

🛡️ Admin Side:
Manual farmer verification removed — now using automated OTP system ✔️

🔥 Tech Stack

Tech	Usage
Android (Java, XML)	App development
Firebase Authentication	User login & OTP verification
Firebase Realtime Database	Storing user info, products, carts, orders
Firebase Storage + Cloudinary	Product image storage
RecyclerView + Adapters	Dynamic product and cart display
Custom Dialogs	Purchase success screens
🏗️ App Structure Overview
FarmerRegisterActivity.java — Handles farmer sign-up and saves their data.

FarmerDashboardActivity.java — Allows farmers to upload their products.

CustomerRegisterActivity.java — Customer signup and login handling.

CustomerDashboardActivity.java — Displays available products filtered by city.

CartActivity.java — Cart management (add, remove, update quantity, buy).

SuccessDialog.java (inside CartActivity) — Shows after successful purchase.

📂 Firebase Realtime Database structure:
Copy
Edit
Farmers/
Customers/
Products/
Cart/
Orders/
🚀 How to Run Locally
Clone the project or download the ZIP.

Open the project in Android Studio.

Connect your app to Firebase (Authentication + Realtime DB + Storage).

Update your google-services.json file (from your Firebase project).

Run the app on an emulator or a physical device! 📱

🎯 Future Improvements (Optional Ideas)
💳 Online payment gateway integration (e.g., Razorpay, PayPal)

🚚 Real-time order tracking for customers

🌟 Farmer rating/review system

🔔 Notification system (e.g., new product uploads)


output



![Screenshot 2025-04-26 160052](https://github.com/user-attachments/assets/94813d84-ea5d-46d7-ba7e-738f7cb546a7)
![Screenshot 2025-04-26 160341](https://github.com/user-attachments/assets/22f4f78d-1278-44ca-a187-f84b94fafa29)
![image](https://github.com/user-attachments/assets/19745a88-ce7a-4309-a856-0112c7f88c4a)
![image](https://github.com/user-attachments/assets/e5024bb1-e654-44a7-9fb2-67769715584e)
![Screenshot 2025-04-26 161228](https://github.com/user-attachments/assets/a100a8f2-de28-415f-9199-412f89c39a89)
![image](https://github.com/user-attachments/assets/f27d1a48-34a4-4756-9347-3e9e9a4d84f0)


MIT License

Copyright (c) 2025 Divya A. Sabane

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.



"Feel free to fork, clone, and use Farmers World under the MIT License. Kindly give credits ❤️."

