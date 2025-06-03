import React, {useState} from 'react';
import {useDispatch} from 'react-redux';
import {addBook} from '../features/bookReducer';
import {createBook} from '../api/api';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import { format } from 'date-fns';

const AddBook = () => {
    const dispatch = useDispatch();
    const [title, setTitle] = useState('');
    const [author, setAuthor] = useState('');
    const [publishedDate, setPublishedDate] = useState<Date | null>(new Date());

    const handleAddBook = async () => {
        if (!publishedDate) {
            alert('Please select a valid date');
            return;
        }
        const formattedDate = format(publishedDate, 'yyyy-MM-dd');
        const newBook = await createBook({title, author: {name: author}, publishedDate: formattedDate });
        dispatch(addBook(newBook));
        setTitle('');
        setAuthor('');
        setPublishedDate(new Date());
    };

    return (
        <div>
            <h2>Add Book</h2>
            <input
                type="text"
                placeholder="Title"
                value={title}
                onChange={(e) => setTitle(e.target.value)}
            />
            <input
                type="text"
                placeholder="Author"
                value={author}
                onChange={(e) => setAuthor(e.target.value)}
            />
            <DatePicker
                selected={publishedDate}
                onChange={(date: React.SetStateAction<Date | null>) => setPublishedDate(date)}
                dateFormat="yyyy-MM-dd"
            />
            <button onClick={handleAddBook}>Add</button>
        </div>
    );
};

export default AddBook;
