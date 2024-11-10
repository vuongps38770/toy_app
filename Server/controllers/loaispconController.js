const loaispcon = require('../models/loaispcon');
const Loaisp = require('../models/loaisp')
const Loaispcon = require('../models/loaispcon')
const Sanpham = require('../models/sanpham')
exports.createLoaiSPCon = async (req,res)=>{
    try {
        const {tenloai} = req.body
        //validate
        if(await Loaispcon.findOne({tenloai}))
            return req.status(409).json({message: "loại này đã tồn tại"})
        //validate id parent
        const {loaiSPID}=req.params;
        const parent=Loaisp.findById(loaiSPID)
        if(!parent)
            return res.status(404).json({message:"Khong tim thay sp"})
        //tao moi
        const newloaispcon = new Loaispcon({...req.body,parentID:loaiSPID})
        await newloaispcon.save();
        //them id vua tao vao parent
        const updatedParent = await Loaisp.findByIdAndUpdate(
            loaiSPID,
            { $addToSet: { listLoaiSPConID: newloaispcon._id } },
            { new: true }
        );
        if (!updatedParent.listLoaiSPConID.includes(newloaispcon._id)) {
            return res.status(500).json({ message: "Chưa thêm được" });
        }
        res.status(200).json(newloaispcon)
    } catch (error) {
        res.status(400).json({message: error.message})
    }
}
// /// dùng promises
// exports.getAllLoaiSPCon = (req,res)=>{
//     Loaispcon.find({isActivate:1})
//     .then(data=>{
//         res.send(data)
//         res.status(200).json(req.body)
//     })
//     .catch(error=>{
//         res.status(500).send({
//             message: error.message||"1 số lỗi đang xảy ra, vui lòng thông báo với liên hợp quốc"
//         })
//     })
// }


exports.removeSanPham = async (req,res)=>{
    try {
        const {loaispconID,sanphamID} = req.params

        await Loaispcon.findByIdAndUpdate(
            loaispconID,
            {$pull:{listsanphamID:sanphamID}},
            {new:true,runValidators:true}
        )

        await Sanpham.findByIdAndUpdate(
            sanphamID,
            {$pull:{listloaispconID:loaispconID}},
            {new:true,runValidators:true}
        )
        return res.status(200).json({message:"xoá ok"})
    } catch (error) {
        return res.status(400).json({message: error.message})
    }
}

exports.addSanPham=async(req,res)=>{
    try {
        const {sanphamID,loaispconID}=req.params
        await Loaispcon.findByIdAndUpdate(
            loaispconID,
            {$addToSet:{listsanphamID:sanphamID}},
            {new:true,runValidators:true}
        )
        await Sanpham.findByIdAndUpdate(
            sanphamID,
            {$addToSet:{listloaispconID:loaispconID}},
            {new:true,runValidators:true}
        )
        return res.status(200).json({message:"Thêm thành công"})
    } catch (error) {
        return res.status(400).json({message: error.message})
    }
}
exports.getLoaiSPConByID = async (req,res)=>{
    try {
        const spcon = await Loaispcon.findById(req.params.id)
        if(!spcon)
            return res.status(404).json({message: "notFound"})
        res.status(200).json(spcon)
    } catch (error) {
        return res.status(400).json({message: error.message})
    }
}
exports.getSPConByParentID= async (req,res)=>{
    
    try {
        const listspcon = await Loaispcon.find({parentID : req.params.id})
        if(!listspcon||listspcon.length==0)
            return res.status(404).json({message: "empty"})
        res.status(200).json(listspcon)
    } catch (error) {
        return res.status(400).json({message: error.message})
    }
}

exports.getSoLuongByParentID= async (req,res)=>{
    try {
        const parentID = req.params.ParentID
        const count = await Loaispcon.countDocuments({parentID:parentID})
        res.status(200).json({count:count})
    } catch (error) {
        return res.status(400).json({message: error.message})
        
    }
}



