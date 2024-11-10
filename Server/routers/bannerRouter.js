const bannerController = require('../controllers/bannerController')
const express = require('express')
const router = express.Router()

router.post('/banner',bannerController.createBanner)
router.put('/banner/edit/:id',bannerController.editBanner)
router.get('/banner/get/:id',bannerController.getBannerById)
module.exports = router
