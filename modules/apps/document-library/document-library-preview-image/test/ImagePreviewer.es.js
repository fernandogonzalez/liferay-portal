/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

import ImagePreviewer from '../src/main/resources/META-INF/resources/preview/js/ImagePreviewer.es';

let component;

describe('document-library-preview-image', () => {
	afterEach(() => {
		if (component) {
			component.dispose();
		}
	});

	it('should render an image previewer', () => {
		component = new ImagePreviewer({
			imageURL: 'image.jpg',
			spritemap: 'icons.svg'
		});

		expect(component).toMatchSnapshot();
	});
});
