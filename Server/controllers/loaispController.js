const { json } = require('express')
const Loaisp = require('../models/loaisp')
const mongoose= require('mongoose')
const Sanpham = require('../models/sanpham')
const Loaispcon = require('../models/loaispcon')
const { populate } = require('../models/users')
exports.createLoaiSP= async (req,res)=>{
    try {
        const {tenloai} = req.body
        if(await Loaisp.findOne({tenloai}))
            return res.status(409).json({message: "Sản phẩm này đã tồn tại"})
        const newLoaisp=new Loaisp(req.body);
        await newLoaisp.save();
        res.status(200).json(newLoaisp);
    } catch (error) {
        res.status(400).json({message:error.message})
    }
}

exports.getAllLoaiSP=async (req,res)=>{
    try {
        const listLoaiSP= await Loaisp.find().select("-listLoaiSPConID");
        if(listLoaiSP.length<=0){
            return res.status(400).json({message: "empty!"})
        }
        res.status(200).json(listLoaiSP)
    } catch (error) {
        res.status(500).json({message:error.message})
    }
}

exports.getLoaiSPByID= async (req,res)=>{
    try {
        const loaisp = await Loaisp.findById(req.params.id)
        if(!loaisp)
            return res.status(404).json({error: "notFound!"})
        res.status(200).json(loaisp)
    } catch (error) {
        res.status(400).json({error: error.message})
    }
}

exports.editLoaiSPByID= async (req,res)=>{
    try {
        const loaisp = await Loaisp.findByIdAndUpdate(req.body._id,req.body,{runValidators: true,new:true}).populate('listLoaiSPConID')
        if(!loaisp)
            return res.status(404).json({error: "notFound!"})
        res.status(200).json(loaisp)
    } catch (error) {
        console.log(error)
        res.status(400).json({message:error.message})
    }
}

exports.unActivate= async (req,res)=>{
    try {
        const loaisp = await Loaisp.findByIdAndUpdate(req.params.id,{isActivate:0},{runValidators: true, new:true})
        if(!loaisp)
            return res.status(404).json({error: "notFound!"})
        res.status(200).json(loaisp)
    } catch (error) {
        res.status(400).json({message:error.message})   
    }       
}            

exports.activate= async (req,res)=>{
    try {
        const loaisp = await Loaisp.findByIdAndUpdate(req.params.id,{isActivate:1},{runValidators: true, new:true})
        if(!loaisp)
            return res.status(404).json({error: "notFound!"})
        res.status(200).json(loaisp)
    } catch (error) {
        res.status(400).json({message:error.message})
    }
}


exports.activateToggle= async (req,res)=>{
    try {
        const loaisp = await Loaisp.findById(req.params.id).select('-listLoaiSPConID')
        if(!loaisp)
            return res.status(404).json({error: "notFound!"})
        loaisp.isActivate=loaisp.isActivate==1?0:1
        const saved =await loaisp.save();
        res.status(200).json(saved)
    } catch (error) {
        res.status(400).json({message:error.message})
    }
}
// exports.editLoaiSP= async (req,res)=>{
//     try {
//         const loaiSP= await Loaisp.findByIdAndUpdate(req.params.id,req.body,{new:true})
//         if(!loaiSP){
//             return res.status(404).json({message:"notFound!"})
//         }
//         return res.status(200).json(loaiSP)
//     } catch (error) {
//         res.status(400).json({message:error.message})
//     }
// }

exports.getAllLoaiSPPopulate=async (req,res)=>{
    try {
        const listLoaiSP= await Loaisp.find().populate({
            path: 'listLoaiSPConID', 
            populate: { path: 'listsanphamID', model: 'sanpham',select: '-thuongHieu' } 
        });
        res.status(200).json(listLoaiSP)
    } catch (error) {
        res.status(400).json({message:error.message})

    }
}



exports.deleteLoaiSP = async (req,res)=>{
    const session = await mongoose.startSession()
    session.startTransaction()
    try {
        const loaiSP = await Loaisp.findById(req.params.id).session(session)
        await Sanpham.updateMany(
            {listloaispconID:{$in:loaiSP.listLoaiSPConID}},
            {$pull:{listloaispconID:{$in:loaiSP.listLoaiSPConID}}},
            {session:session}
        )
        await Loaispcon.deleteMany(
            {_id:{$in:loaiSP.listLoaiSPConID}},
            {session:session}
        )
        await session.commitTransaction()
        session.endSession()
        await Loaisp.findByIdAndDelete(req.params.id)
        res.status(200).json({message: "Ok"})
    } catch (error) {
        await session.abortTransaction()
        session.endSession()
        res.status(400).json({message:error.message})
    }
}