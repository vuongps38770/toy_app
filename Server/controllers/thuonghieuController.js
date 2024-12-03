const thuonghieu = require('../models/thuonghieu');
const ThuongHieu = require('../models/thuonghieu')

exports.getAllThuongHieu= async (req, res)=>{
    try {
        const list = await ThuongHieu.find();
        res.status(200).json(list)
    } catch (error) {
        res.status(400).json({message:error.message})
    }

}
exports.getAllThuongHieuActivated= async (req, res)=>{
    try {
        const list = await ThuongHieu.find({activated:1});
        res.status(200).json(list)
    } catch (error) {
        res.status(400).json({message:error.message})
    }
}
exports.suaThuongHieu= async (req,res)=>{
    try {
        const thuonghieu = await ThuongHieu.findByIdAndUpdate(req.body._id,req.body,{new:true})
        if(!thuonghieu)
            return res.status(404).json({message: "not found"})
        res.status(200).json(thuonghieu)
    } catch (error) {
        res.status(400).json({message:error.message})
        await console.log(error)
    }
}
exports.createThuongHieu = async( req, res)=>{
    try {
        const {tenthuonghieu}= req.body
        req.body.isactive=1;
        const thuonghieu= await ThuongHieu.findOne({tenthuonghieu:tenthuonghieu})
        if(thuonghieu)
            return res.status(409).json({mesage: "Đã tồn tại"})
        const newThuongHieu=new ThuongHieu(req.body)
        newThuongHieu.activated=1;
        await newThuongHieu.save()
        res.status(200).json({message:"thêm thành công"})
    } catch (error) {
        res.status(400).json({message:error.message})
    }
}

exports.checkKhongTonTai= async (req,res)=>{
    try {
        const {tenthuonghieu}= req.query
        const thuonghieu= await ThuongHieu.findOne({tenthuonghieu:tenthuonghieu})
        if(thuonghieu)
            return res.status(409).json({message: "đã tồn tại"})
        res.status(200).json({message:"ok"})
    } catch (error) {
        res.status(400).json({message:error.message})
    }
}

