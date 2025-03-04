const express = require('express');
const cors = require('cors');
const app = express();

app.use(cors());

app.get('/resource_omega', (req, res) => {
    res.send('Hello from team OMEGA');
});

const port = 3000;
app.listen(port, () => {
    console.log(`Server running on port ${port}`);
});