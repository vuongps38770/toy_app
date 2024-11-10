const ThuongHieuController = require('../controllers/thuonghieuController')

const express = require('express')
const router = express.Router()
router.get("/thuonghieu/getall",ThuongHieuController.getAllThuongHieu)
router.post("/thuonghieu/create",ThuongHieuController.createThuongHieu)
router.post("/thuonghieu/checkKhongTonTai",ThuongHieuController.checkKhongTonTai)

module.exports= router