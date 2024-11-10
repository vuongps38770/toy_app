
const Anhsp = require('../models/anhsp')

exports.addanh=async (req, res) => {
    try {
        const newAnh = new Anhsp(req.body);
        await newAnh.save()
        res.status(200).json({message:"Lưu thành công"})
    }catch(error){
        res.status(400).json({message:error.message})
    }
}

exports.editAnh=async (req, res) => {
    try {
        const anhsp= await Anhsp.findByIdAndUpdate(req.params.id,{url:req.body},{new:true,runValidators:true})
        if(!anhsp)
            res.status(404).json({message:"ảnh không tồn tại"})
        res.status(200).json({message:"Lưu thành công"})
    }catch(error){
        res.status(400).json({message:error.message})
    }
}

exports.getAllAnh=async(req,res)=>{
    try {
        const listAnh=await Anhsp.find()
        if(!listAnh.length>0)
            return res.status(400).json({message:"empty!"})
        res.status(200).json(listAnh)
    } catch (error) {
        res.status(400).json({message:error.message})
    }
}