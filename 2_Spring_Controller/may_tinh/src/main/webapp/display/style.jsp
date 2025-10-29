body {
font-family: "Segoe UI", Arial, sans-serif;
background-color: #f4f6f9;
margin: 0;
padding: 20px;
color: #333;
}

h1 {
text-align: center;
color: #2c3e50;
}

a {
text-decoration: none;
color: #3498db;
font-weight: 500;
}

a:hover {
color: #1abc9c;
}

table {
border-collapse: collapse;
width: 90%;
margin: 20px auto;
background-color: #fff;
box-shadow: 0 2px 8px rgba(0,0,0,0.1);
border-radius: 8px;
overflow: hidden;
}

th, td {
padding: 12px 16px;
text-align: left;
}

th {
background-color: #3498db;
color: #fff;
text-transform: uppercase;
font-size: 14px;
letter-spacing: 1px;
}

tr:nth-child(even) {
background-color: #f9f9f9;
}

tr:hover {
background-color: #ecf6fd;
}

td a {
margin: 0 5px;
padding: 5px 10px;
border-radius: 4px;
}

td a[href*="edit"] {
background-color: #f1c40f;
color: #fff;
}

td a[href*="delete"] {
background-color: #e74c3c;
color: #fff;
}

td a[href*="edit"]:hover {
background-color: #d4ac0d;
}

td a[href*="delete"]:hover {
background-color: #c0392b;
}

a[href*="create"] {
display: inline-block;
margin: 10px auto;
background-color: #2ecc71;
color: #fff;
padding: 10px 16px;
border-radius: 6px;
transition: 0.2s;
}

a[href*="create"]:hover {
background-color: #27ae60;
}
