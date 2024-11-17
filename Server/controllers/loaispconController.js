const loaispcon = require('../models/loaispcon');
const Loaisp = require('../models/loaisp')
const Loaispcon = require('../models/loaispcon')
const Sanpham = require('../models/sanpham');
const sanpham = require('../models/sanpham');
const { default: mongoose } = require('mongoose');
exports.createLoaiSPCon = async (req,res)=>{
    /*
        params:ParentID,loaiSPID
    */
    try {
        const {tenloai} = req.body
        const ParentID = req.params.ParentID
        //validate id parent
        const parent=await Loaisp.findById(ParentID)
        if(!parent)
            return res.status(404).json({message:"Khong tim thay sp"})
        //validate
        if(await Loaispcon.findOne({tenloai,ParentID}))
            return res.status(409).json({message: "loại này đã tồn tại"})
        //tao moi
        const newloaispcon = new Loaispcon({...req.body,parentID:ParentID})
        await newloaispcon.save();
        //them id vua tao vao parent
        const updatedParent = await Loaisp.findByIdAndUpdate(
            ParentID,
            { $addToSet: { listLoaiSPConID: newloaispcon._id } },
            { new: true }
        );
        if (!updatedParent.listLoaiSPConID.includes(newloaispcon._id)) {
            return res.status(500).json({ message: "Chưa thêm được" });
        }
        res.status(200).json(newloaispcon)
    } catch (error) {
        res.status(400).json({message:error.message})
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
//             message: error||"1 số lỗi đang xảy ra, vui lòng thông báo với liên hợp quốc"
//         })
//     })
// }


exports.removeSanPham = async (req,res)=>{
    const session = await mongoose.startSession()
    session.startTransaction()
    try {
        const {loaispconID,sanphamID} = req.params

        await Loaispcon.findByIdAndUpdate(
            loaispconID,
            {$pull:{listsanphamID:sanphamID}},
            {new:true,runValidators:true,session}
        )

        await Sanpham.findByIdAndUpdate(
            sanphamID,
            {$pull:{listloaispconID:loaispconID}},
            {new:true,runValidators:true,session}
        )
        await session.commitTransaction()
        session.endSession()
        return res.status(200).json({message:"xoá ok"})
    } catch (error) {
        await session.abortTransaction()
        session.endSession()
        return res.status(400).json({message:error.message})
    }
}

exports.addSanPham=async(req,res)=>{
    const session = await mongoose.startSession()
    session.startTransaction()
    try {
        const {sanphamID,loaispconID}=req.params
        await Loaispcon.findByIdAndUpdate(
            loaispconID,
            {$addToSet:{listsanphamID:sanphamID}},
            {new:true,runValidators:true,session}
        )
        await Sanpham.findByIdAndUpdate(
            sanphamID,
            {$addToSet:{listloaispconID:loaispconID}},
            {new:true,runValidators:true,session}
        )
        await session.commitTransaction()
        session.endSession()
        return res.status(200).json({message:"Thêm thành công"})
    } catch (error) {
        await session.abortTransaction()
        session.endSession()
        return res.status(400).json({message:error.message})
    }
}
exports.getLoaiSPConByID = async (req,res)=>{
    try {
        const spcon = await Loaispcon.findById(req.params.id)
        if(!spcon)
            return res.status(404).json({message: "notFound"})
        res.status(200).json(spcon)
    } catch (error) {
        return res.status(400).json({message:error.message})
    }
}
exports.getSPConByParentID= async (req,res)=>{
    
    try {
        console.log(req.params.ParentID);
        const parent =await Loaisp.findById(req.params.ParentID)
        if(!parent)
            return res.status(404).json({message: "ko tồn tại id cha"})
        const listspcon = await Loaispcon.find({parentID : req.params.ParentID}).select("-listsanphamID")
        res.status(200).json(listspcon)
    } catch (error) {
        return res.status(400).json({message:error.message})
    }
}

exports.getSoLuongByParentID= async (req,res)=>{
    try {
        const parentID = req.params.ParentID
        const count = await Loaispcon.countDocuments({parentID:parentID})
        res.status(200).json(count)
    } catch (error) {
        return res.status(400).json({message: error.message})
        
    }
}
exports.editLoaiSPConByID= async (req,res)=>{
    try {
        const find = await Loaispcon.findOne({tenloai: req.body.tenloai,parentID:req.body.parentID})
        if(find)
            return res.status(409).json({message: "Tên đã tồn tại"})
        const loaispcon = await Loaispcon.findByIdAndUpdate(req.body._id,req.body,{new:true,runValidators:true})
        res.status(200).json(loaispcon)
    } catch (error) {
        return res.status(400).json({error: error.message})
    }
}


exports.activeToggle = async (req,res)=>{
    try {
        const loaispcon = await Loaispcon.findByIdAndUpdate(
            req.body._id
            ,{isActivate:(req.body.isActivate==1)?0:1}
            ,{new:true,runValidators:true})
            .select("-listsanphamID")
        res.status(200).json(loaispcon)
    } catch (error) {
        return res.status(400).json({error: error.message})
    }
}

exports.deleteLSPConByID = async(req, res)=>{
    const session = await mongoose.startSession()
    session.startTransaction()
    try {
        const loaispcon = await Loaispcon.findById(req.params.id).session(session)
        await Sanpham.updateMany(
            {listloaispconID:{$in:loaispcon.listsanphamID}},
            {$pull:{listloaispconID:{$in:loaispcon.listsanphamID}}},
            {session:session}
        )
        // for(let sanpham of loaispcon.listsanphamID){
        //     await sanpham.findByIdAndUpdate(sanpham._id,
        //         {$pull: {listLoaiSPConID:req.params.id}},
        //         {session}
        //     )
        // }
        await loaispcon.deleteOne({_id:req.params.id},
            {session}
        )
        await session.commitTransaction();
        session.endSession()
        res.status(200).json({message: "thành công"})
    } catch (error) {
        await session.abortTransaction()
        session.endSession()
        return res.status(400).json({error: error.message})

    }
}