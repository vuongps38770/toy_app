const mongoose = require('mongoose')
const Sanpham = require('../models/sanpham')
exports.createsp= async(req,res)=>{
    try {
        const {tensanpham} = req.body
        const search = await Sanpham.findOne({tensanpham})
        if(search)
            return res.status(409).json({message: "Sản phẩm không được trùng với sản phẩm khác trong hệ thống"})
        const newSP= new Sanpham(req.body)
        await newSP.save();
        res.status(200).json(newSP)
    } catch (error) {
        res.status(400).json({message:error.message})
    }
}


exports.editSPbyID = async(req,res)=>{
    try {
        const org =  await Sanpham.findById(req.params.id);
        if(!org){
            return res.status(404).json({message:"notFound"})
        }
        const sanpham = await Sanpham.findByIdAndUpdate(req.params.id,req.body,{runValidators:true,new:true}).populate("thuonghieu").select("-listloaispconID")
        if(!sanpham)
            return res.status(404).json({message:"not found!"}) 
        res.status(200).json(sanpham)       
    } catch (error) {
        res.status(400).json({message:error.message})
    }
}

exports.activate = async (req,res)=>{
    try {
        const sanpham = await Sanpham.findByIdAndUpdate(req.params.id,{isActivate:1},{runValidators:true,new:true})
        if(!sanpham)
            return res.status(404).json({message:"not found!"}) 
        res.status(200).json(sanpham)
    } catch (error) {
        res.status(400).json({message:error.message})
    }
}


exports.unactivate = async (req,res)=>{
    try {
        const sanpham = await Sanpham.findByIdAndUpdate(req.params.id,{isActivate:0},{runValidators:true,new:true})
        if(!sanpham)
            return res.status(404).json({message:"not found!"}) 
        res.status(200).json(sanpham)
    } catch (error) {
        res.status(400).json({message:error.message})
        
    }
}
exports.getAllsanpham= async (req, res)=>{
    try {
        const listSP=await Sanpham.find().populate("thuonghieu").select("-listloaispconID")
        return res.status(200).json(listSP)
    } catch (error) {
        res.status(400).json({message:error.message})
    }
}

exports.getAllsanphamByLoaiSPconID= async (req,res)=>{
    try {
        const listsp= await Sanpham.find({
            listloaispconID:req.params.id
        }).populate("thuonghieu").select("-listloaispconID")
        res.status(200).json(listsp)
    } catch (error) {
        res.status(400).json({message:error.message})
    }
} 

exports.getAllsanphamNotHaveLoaiSPconID= async (req,res)=>{
    try {
        const listsp= await Sanpham.find({
            listloaispconID:{$ne:req.params.id}
        }).populate("thuonghieu").select("-listloaispconID")
        res.status(200).json(listsp)
    } catch (error) {
        res.status(400).json({message:error.message})
    }
} 


exports.getAllsanphamByTenhang= async(req,res)=>{
    try {
        const {thuonghieu}= req.body
        const listSp = await Sanpham.find({thuonghieu:thuonghieu})
        return (listSp.length>0) ?res.status(200).json(listSp):res.status(400).json({message:"empty"})
    } catch (error) {
        res.status(400).json({message:error.message})
    }
}

