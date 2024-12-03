const SanphamController = require("../controllers/sanphamController")
const express = require('express')
const router=express.Router()

router.post("/sanpham/create",SanphamController.createsp)
router.get("/sanpham/getAllsanpham",SanphamController.getAllsanpham)
router.get("/sanpham/getAllsanphamByLoaiSPconID/:id",SanphamController.getAllsanphamByLoaiSPconID)
router.put("/sanpham/editSPByID/:id",SanphamController.editSPbyID)
router.put("/sanpham/editSPByID/:id",SanphamController.editSPbyID)
router.get("/sanpham/getAllsanphamNotHaveLoaiSPconID/:id",SanphamController.getAllsanphamNotHaveLoaiSPconID)

module.exports = router


