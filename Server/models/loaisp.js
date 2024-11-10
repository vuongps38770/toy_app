const mongoose= require('mongoose')
const Schema = mongoose.Schema;


const loaisp= new Schema({
    tenloai:{
        type:String,
        unique:true,   
        required:true, 
    },
    listLoaiSPConID:[{
        type: mongoose.Schema.Types.ObjectID,
        ref:"loaispcon",
    }],
    isActivate:{
        type:Number,
        required:true, 
    }
},{
    collection:'loaisp',
    timestamps:true
})


module.exports=mongoose.model('loaisp',loaisp,'loaisp')
