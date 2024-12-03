const ThuongHieuController = require('../controllers/thuonghieuController')

const express = require('express')
const router = express.Router()
router.get("/thuonghieu/getall",ThuongHieuController.getAllThuongHieu)
router.get("/thuonghieu/getAllThuongHieuActivated",ThuongHieuController.getAllThuongHieuActivated)
router.post("/thuonghieu/create",ThuongHieuController.createThuongHieu)
router.post("/thuonghieu/checkKhongTonTai",ThuongHieuController.checkKhongTonTai)
router.put("/thuonghieu/edit",ThuongHieuController.suaThuongHieu)

module.exports= router