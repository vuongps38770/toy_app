const mongoose= require('mongoose')
const Schema = mongoose.Schema;

const loaispcon= new Schema({
    parentID:{
        type: mongoose.Schema.Types.ObjectID,
        ref:"loaisp",
        required:true,
    },
    listsanphamID:[{
        type: mongoose.Schema.Types.ObjectID,
        ref:"sanpham",
    }],
    tenloai:{
        type:String,
        required:true, 
    },
    isActivate:{
        type:Number,
        required:true, 
    }   
},{
    collection:'loaispcon',
    timestamps:true,
})

module.exports=mongoose.model('loaispcon',loaispcon,'loaispcon')
