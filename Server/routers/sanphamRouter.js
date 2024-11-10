const SanphamController = require("../controllers/sanphamController")
const express = require('express')
const router=express.Router()

router.post("/sanpham/create",SanphamController.createsp)
module.exports = router


