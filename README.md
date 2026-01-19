# 📦 Inventory Management System (Java Console Application)

Inventory Management System is a **menu-driven Java console project** that helps manage products in a store/warehouse.  
It supports product addition, stock update, sorting, transaction history, inventory statistics, low-stock alerts, and undo operations using core Java and Collections Framework.

---

## 🚀 Features

✅ Add new products with unique SKU  
✅ Prevent duplicate SKU entries (HashSet)  
✅ Update product quantity  
✅ Undo last stock update using Stack  
✅ Display products sorted by:
- SKU
- Name
- Price
- Inventory Value  

✅ Low Stock Alerts (stock less than 10)  
✅ Transaction History tracking  
✅ Inventory Statistics with category-wise breakdown  
✅ Clean console output formatting

---

## 🧾 Project Overview

This project is built using **Object Oriented Programming (OOP)** and **Java Collections Framework** to efficiently manage inventory.

### ✅ Key Concepts Used
- OOP Concepts: Class, Object, Encapsulation
- Comparable & Comparator for sorting
- Java Collections:
  - HashSet
  - TreeSet
  - LinkedList
  - Stack
  - Queue
  - ArrayList
  - HashMap

---

## 🏗️ System Design

### ✅ Product Class
Each product has the following details:

| Field | Description |
|------|-------------|
| `sku` | Unique product ID |
| `name` | Product name |
| `category` | Product category (Electronics, Grocery, etc.) |
| `price` | Price of product |
| `quantity` | Quantity in stock |
| `lastUpdated` | Date/time of last update |

✅ It implements `Comparable<Product>` to sort naturally by SKU.

---

### ✅ Comparator Classes
Custom sorting is supported using Comparators:

✅ **PriceComparator** → Sort by Price (Low → High)  
✅ **ValueComparator** → Sort by Inventory Value (High → Low)

---

### ✅ InventoryManagementSystem Class
This class manages all inventory operations using different collections:

| Data Structure | Used For |
|--------------|----------|
| `HashSet<Product>` | Unique product storage (prevents duplicates by SKU) |
| `TreeSet<Product>` | Automatically sorted products by SKU |
| `LinkedList<String>` | Transaction history (latest at top) |
| `Stack<Product>` | Undo feature |
| `Queue<Product>` | Low stock product alert system |

---

## 🖥️ Menu Options (Console)

When you run the program, you get the following menu:

1. Add Product  
2. Update Product Quantity  
3. Display Products (Sorted)  
4. Low Stock Alerts  
5. Transaction History  
6. Inventory Statistics  
7. Undo Last Update  
8. Exit  

---

## ⚙️ How to Run the Project

### ✅ Requirements
- Java JDK 8 or above
- VS Code / IntelliJ / Eclipse (any Java IDE)
- Terminal / Command Prompt

---

### ✅ Steps to Run

#### 1️⃣ Clone the repository
```bash
git clone https://github.com/your-username/Inventory-Management-System-Java.git
