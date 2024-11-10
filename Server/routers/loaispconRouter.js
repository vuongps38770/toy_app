const express = require('express')
const router = express.Router()
const LoaispconController = require('../controllers/loaispconController')
router.post('/loaispcon/create/:loaiSPID',LoaispconController.createLoaiSPCon)
router.get('/loaispcon/getallspconByParentID/:id',LoaispconController.getSPConByParentID)
router.get('/loaispcon/getLoaiSPConByID/loaisanphamconID',LoaispconController.addSanPham)
router.put('/loaispcon/:loaispconID/removeSanPham/:sanphamID',LoaispconController.removeSanPham)
router.put('/loaispcon/:loaispconID/addSanPham/:sanphamID',LoaispconController.addSanPham)
router.get('/loaispcon/getSoLuongByParentID/:ParentID',LoaispconController.getSoLuongByParentID)

module.exports =router;