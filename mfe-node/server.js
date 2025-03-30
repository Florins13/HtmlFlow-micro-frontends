const express = require('express');
const cors = require('cors');
const app = express();

app.use(cors());

app.get('/resource_omega', (req, res) => {
    res.send('<p>Hello from team OMEGA</p>');
});

const port = 3000;
app.listen(port, () => {
    console.log(`Server running on port ${port}`);
});