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
        res.status(400).json({message: error.message})
    }
}


exports.editSPbyID = async(req,res)=>{
    try {
        const sanpham = await Sanpham.findByIdAndUpdate(req.params.id,req.body,{runValidators:true,new:true})
        if(!sanpham)
            return res.status(404).json({message:"not found!"}) 
        res.status(200).json(sanpham)       
    } catch (error) {
        res.status(400).json({message: error.message})
    }
}

exports.activate = async (req,res)=>{
    try {
        const sanpham = await Sanpham.findByIdAndUpdate(req.params.id,{isActivate:1},{runValidators:true,new:true})
        if(!sanpham)
            return res.status(404).json({message:"not found!"}) 
        res.status(200).json(sanpham)
    } catch (error) {
        res.status(400).json({message: error.message})
        
    }
}


exports.unactivate = async (req,res)=>{
    try {
        const sanpham = await Sanpham.findByIdAndUpdate(req.params.id,{isActivate:0},{runValidators:true,new:true})
        if(!sanpham)
            return res.status(404).json({message:"not found!"}) 
        res.status(200).json(sanpham)
    } catch (error) {
        res.status(400).json({message: error.message})
        
    }
}
exports.getAllsanpham= async (req, res)=>{
    try {
        const listSP=Sanpham.find()
        if(!listSP)
            return res.status(400).json({message:"empty"})
        return res.status(400).json(listSP)
    } catch (error) {
        res.status(400).json({message: error.message})
        
    }
}

exports.getAllsanphamByLoaiSPconID= async (req,res)=>{
    try {
        const {loaispconID } = req.params
        const listsp= await Sanpham.find({
            listloaispconID:loaispconID
        })
        if(!listsp.length>0)
            return res.status(400).json({message: "empty!"})
        res.status(200).json(listsp)
    } catch (error) {
        res.status(400).json({message: error.message})
    }
}


exports.getAllsanphamByTenhang= async(req,res)=>{
    try {
        const {thuonghieu}= req.body
        const listSp = await Sanpham.find({thuonghieu:thuonghieu})
        return (listSp.length>0) ?res.status(200).json(listSp):res.status(400).json({message:"empty"})
    } catch (error) {
        res.status(400).json({message: error.message})
    }
}

