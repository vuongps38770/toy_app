const Banner = require('../models/banner')

exports.createBanner= async (req,res)=>{
    try {
        const banner = new Banner(req.body)
        await banner.save()
        res.status(200).json({})
    } catch (error) {
        res.status(400).json({message: error.message})
    }
}

exports.editBanner = async (req,res)=>{
    try {
        const newBanner = await Banner.findByIdAndUpdate(req.params.id,req.body,{new:true,runValidators:true})
        if(!newBanner)
            res.status(404).json({message:"ko thấy"})
        res.status(200).json(newBanner)

    } catch (error) {
        res.status(400).json({message:error.message})
    }
}

exports.getBannerById = async (req,res)=>{
    try {
        const banner = await Banner.findById(req.params.id)
        if(!banner)
            res.status(404).json({message:"ko thấy"})
        res.status(200).json(banner)
    } catch (error) {
        res.status(400).json({message:error.message})
    }
}

