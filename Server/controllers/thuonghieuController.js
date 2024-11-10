const ThuongHieu = require('../models/thuonghieu')

exports.getAllThuongHieu= async (req, res)=>{
    try {
        const list = await ThuongHieu.find();
        res.status(200).json(list)
    } catch (error) {
        res.status(400).json({message: error})
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
        await newThuongHieu.save()
        res.status(200).json({message:"thêm thành công"})
    } catch (error) {
        res.status(400).json({message: error})
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
        res.status(400).json({message: error})
    }
}

